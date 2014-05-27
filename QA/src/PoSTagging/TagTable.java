package PoSTagging;

import Models.*;

import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jacobdaniel
 * Date: 4/28/14
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class TagTable {
	private Map<PartOfSpeechTag, Integer> tagFrequencies;
	private Map<String, Map<PartOfSpeechTag, Integer>> wordTagFrequencies;
	private Map<PartOfSpeechTag, Map<PartOfSpeechTag, Integer>> precedingPosFrequencies;
    private Map<PartOfSpeechTag, Map<List<PartOfSpeechTag>, Integer>> precedingTwoPosFrequencies;
	private PartOfSpeechTag precedingPos = null;
    private PartOfSpeechTag precedingPrecedingPos = null;
	private long totalFrequency = 0;

	public TagTable(boolean readCorpus){
		tagFrequencies = new HashMap<PartOfSpeechTag, Integer>();
		wordTagFrequencies = new HashMap<String, Map<PartOfSpeechTag, Integer>>();
		precedingPosFrequencies = new HashMap<PartOfSpeechTag, Map<PartOfSpeechTag, Integer>>();
        precedingTwoPosFrequencies = new HashMap<PartOfSpeechTag, Map<List<PartOfSpeechTag>, Integer>>();

		if(readCorpus){
			getDataFromCorpus();
			printToFiles();
		}
		else getDataFromFiles();
	}

	public long getTotalFrequency(){
		return totalFrequency;
	}

    public int getTotalWords(){
        return wordTagFrequencies.keySet().size();
    }

    public Set<String> getAllWords(){
        return wordTagFrequencies.keySet();
    }

	public int getTagFrequency(PartOfSpeechTag tag){
		return tagFrequencies.get(tag);
	}

	public int getWordTagFrequency(String word, PartOfSpeechTag tag){
		return wordTagFrequencies.get(word).get(tag);
	}

	public int getPrecedingTagFrequency(PartOfSpeechTag first, PartOfSpeechTag second){
        if(precedingPosFrequencies.get(second).containsKey(first))
		    return precedingPosFrequencies.get(second).get(first);
        else return 0;
	}

	public boolean isWordInTable(String word){
		return wordTagFrequencies.containsKey(word);
	}

	public boolean isTagInTable(PartOfSpeechTag tag){
		return tagFrequencies.containsKey(tag);
	}

	public boolean isWordTagPairInTable(String word, PartOfSpeechTag tag){
		return wordTagFrequencies.get(word).containsKey(tag);
	}

	public boolean isTagPrecededByTag(PartOfSpeechTag first, PartOfSpeechTag second){
		return precedingPosFrequencies.get(second).containsKey(first);
	}

    public boolean isTagPrecededByTags(PartOfSpeechTag first, PartOfSpeechTag second, PartOfSpeechTag third){
        Map<List<PartOfSpeechTag>,Integer> map = precedingTwoPosFrequencies.get(third);
        boolean result = false;
        for(List<PartOfSpeechTag> preceding: map.keySet()){
            if(preceding.get(0) == second && preceding.get(1) == first)
                result = true;
        }
        return result;
    }

    public int getPrecedingTagsFrequency(PartOfSpeechTag first, PartOfSpeechTag second, PartOfSpeechTag third){
        Map<List<PartOfSpeechTag>,Integer> map = precedingTwoPosFrequencies.get(third);
        int frequency = 0;
        for(List<PartOfSpeechTag> preceding : map.keySet()){
            if(preceding.contains(first) && preceding.contains(second))
                frequency = map.get(preceding);
        }
        return frequency;
    }

	public Set<PartOfSpeechTag> getWordTags(String word){
		return wordTagFrequencies.get(word).keySet();
	}

	private void getDataFromCorpus(){
		File rootDir = new File("Data/Penn Treebank Samples");
		File[] files = rootDir.listFiles();

		for(File file: files){
			readFile(file);
		}

        rootDir = new File("Data/AtTask Samples");
        files = rootDir.listFiles();
        for(File file : files){
            readFile(file);
        }
	}

	private void getDataFromFiles(){
		try {
			BufferedReader reader = new BufferedReader((new FileReader(new File("Data/TagTableData/tagFrequencies.txt"))));
			BufferedReader reader2 = new BufferedReader(new FileReader(new File("Data/TagTableData/wordTagFrequencies.txt")));
			BufferedReader reader3 = new BufferedReader(new FileReader(new File("Data/TagTableData/precedingPosFrequencies.txt")));
            BufferedReader reader4 = new BufferedReader(new FileReader(new File("Data/TagTableData/precedingTwoPosFrequencies.txt")));

			try {
				String line = reader.readLine();
				while(line != null){
					String[] fields = line.split(",");
					PartOfSpeechTag tag = findMatchingPartOfSpeechTag(fields[0].split(":")[1]);
					Integer frequency = Integer.parseInt(fields[1].split(":")[1]);
					tagFrequencies.put(tag,frequency);
					totalFrequency += frequency;
					line = reader.readLine();
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				String line = reader2.readLine();
				while(line != null){
					String[] wordTag = line.split("\\{");
					String word = wordTag[0];
					String[] tagFrequencies = wordTag[1].replace("}","").split(";");
					HashMap<PartOfSpeechTag,Integer> temp = new HashMap<PartOfSpeechTag, Integer>();
					for(String tf : tagFrequencies){
						String[] fields = tf.split(",");
						PartOfSpeechTag tag = findMatchingPartOfSpeechTag(fields[0].split(":")[1]);
						Integer frequency = Integer.parseInt(fields[1].split(":")[1]);
						temp.put(tag,frequency);
					}
					wordTagFrequencies.put(word,temp);
					line = reader2.readLine();
				}
				reader2.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try{
				String line = reader3.readLine();
				while(line != null){
					String[] tagInfo = line.split("\\{");
					String stringTag = tagInfo[0];
					PartOfSpeechTag tag = findMatchingPartOfSpeechTag(stringTag);
					String[] tagFrequencies = tagInfo[1].replace("}","").split(";");
					HashMap<PartOfSpeechTag,Integer> temp = new HashMap<PartOfSpeechTag, Integer>();
					for(String tf : tagFrequencies){
						String[] fields = tf.split(",");
						PartOfSpeechTag pos = findMatchingPartOfSpeechTag(fields[0].split(":")[1]);
						Integer frequency = Integer.parseInt(fields[1].split(":")[1]);
						temp.put(pos,frequency);
					}
					precedingPosFrequencies.put(tag,temp);
					line = reader3.readLine();
				}
				reader3.close();
			} catch (IOException e){
				e.printStackTrace();
			}

            try{
                String line = reader4.readLine();
                while(line != null){
                    String[] tagInfo = line.split("\\{");
                    String stringTag = tagInfo[0];
                    PartOfSpeechTag tag = findMatchingPartOfSpeechTag(stringTag);
                    String[] tagFrequencies = tagInfo[1].replace("}","").split(";");
                    HashMap<List<PartOfSpeechTag>, Integer> temp = new HashMap<List<PartOfSpeechTag>, Integer>();


                    for(String tf : tagFrequencies){
                        String[] fields = tf.split(",");
                        PartOfSpeechTag first = findMatchingPartOfSpeechTag(fields[0].split(":")[1].trim().split(" ")[0]);
                        PartOfSpeechTag second = findMatchingPartOfSpeechTag(fields[0].split(":")[1].trim().split(" ")[1]);
                        Integer frequency = Integer.parseInt(fields[1].split(":")[1]);
                        ArrayList<PartOfSpeechTag> preceding = new ArrayList<PartOfSpeechTag>();
                        preceding.add(first);
                        preceding.add(second);
                        temp.put(preceding,frequency);
                    }
                    precedingTwoPosFrequencies.put(tag,temp);
                    line = reader4.readLine();
                }
                reader4.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void printToFiles(){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(("Data/TagTableData/tagFrequencies.txt")));
			BufferedWriter writer2 = new BufferedWriter(new FileWriter("Data/TagTableData/wordTagFrequencies.txt"));
			BufferedWriter writer3 = new BufferedWriter(new FileWriter("Data/TagTableData/precedingPosFrequencies.txt"));
            BufferedWriter writer4 = new BufferedWriter(new FileWriter("Data/TagTableData/precedingTwoPosFrequencies.txt"));

			for(PartOfSpeechTag tag : tagFrequencies.keySet()){
				writer.write("Tag:" + tag.name() + ", Frequency:" + tagFrequencies.get(tag)+"\n");
				totalFrequency += tagFrequencies.get(tag);
			}
			writer.close();

			for(String word: wordTagFrequencies.keySet()){
				writer2.write(word+"{");
				for(PartOfSpeechTag tag : wordTagFrequencies.get(word).keySet()){
					writer2.write(" Tag:"+tag.name()+", Frequency:"+wordTagFrequencies.get(word).get(tag)+";");
				}
				writer2.write("}\n");
			}
			writer2.close();

			for(PartOfSpeechTag tag : precedingPosFrequencies.keySet()){
				writer3.write(tag.name()+"{");
				for(PartOfSpeechTag preceding : precedingPosFrequencies.get(tag).keySet()){
					writer3.write(" Tag:"+preceding.name()+", Frequency:"+precedingPosFrequencies.get(tag).get(preceding)+";");
				}
				writer3.write("}\n");
			}
			writer3.close();

            for(PartOfSpeechTag tag : precedingTwoPosFrequencies.keySet()){
                writer4.write(tag.name()+"{");
                for(List<PartOfSpeechTag> preceding : precedingTwoPosFrequencies.get(tag).keySet()){
                    writer4.write(" Tags: ");
                    for(PartOfSpeechTag pos : preceding){
                        writer4.write(pos.name()+" ");
                    }
                    writer4.write(", Frequency:" + precedingTwoPosFrequencies.get(tag).get(preceding)+";");
                }
                writer4.write("}\n");
            }
            writer4.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void readFile(File file){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			try {
				String line = reader.readLine();
				while(line != null){
					if(line.trim().length() > 0)
						parseLine(line);
					line = reader.readLine();
				}
		    reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void parseLine(String line){
		line = line.replace("\t","").replace("[","").replace("]","").replace("=","").trim();
		String[] words = line.split(" ");

		for(String word: words){
			String stringTag = word.replace(".","").replace("(","").replace(")","").replace("{","")
				.replace("}","").replace("*", "").replace(",","").replace(":","").replace("--", " ").trim();
			if(stringTag.split("/").length > 1){
				word =  stringTag.split("/")[0].toLowerCase();
				stringTag = stringTag.split("/")[1].split("-")[0];

				PartOfSpeechTag pos = findMatchingPartOfSpeechTag(stringTag);
				if(pos != null && word.length() > 0){
					//update tag frequencies
					if(!tagFrequencies.containsKey(pos))
						tagFrequencies.put(pos,1);
					else tagFrequencies.put(pos,tagFrequencies.get(pos)+1);

					//update word-tag frequencies
					if(!wordTagFrequencies.containsKey(word)){
						HashMap<PartOfSpeechTag,Integer> temp = new HashMap<PartOfSpeechTag, Integer>();
						temp.put(pos,1);
						wordTagFrequencies.put(word,temp);
					}
					else if(!wordTagFrequencies.get(word).containsKey(pos)){
						Map<PartOfSpeechTag,Integer> temp = wordTagFrequencies.get(word);
						temp.put(pos,1);
						wordTagFrequencies.put(word,temp);
					}
					else{
						Map<PartOfSpeechTag,Integer> temp = wordTagFrequencies.get(word);
						temp.put(pos,temp.get(pos)+1);
						wordTagFrequencies.put(word,temp);
					}

					//update preceding pos frequencies
					if(precedingPos != null){
						if(!precedingPosFrequencies.containsKey(pos)){
							HashMap<PartOfSpeechTag, Integer> temp = new HashMap<PartOfSpeechTag, Integer>();
							temp.put(precedingPos,1);
							precedingPosFrequencies.put(pos,temp);
						}
						else if(!precedingPosFrequencies.get(pos).containsKey(precedingPos)){
							Map<PartOfSpeechTag, Integer> temp = precedingPosFrequencies.get(pos);
							temp.put(precedingPos,1);
							precedingPosFrequencies.put(pos,temp);
						}
						else{
							Map<PartOfSpeechTag, Integer> temp = precedingPosFrequencies.get(pos);
							temp.put(precedingPos, temp.get(precedingPos)+1);
							precedingPosFrequencies.put(pos,temp);
						}
					}

                    //update preceding two pos frequencies
                    if(precedingPrecedingPos != null && precedingPos != null){
                        boolean added = false;
                        if(!precedingTwoPosFrequencies.containsKey(pos)){
                            ArrayList<PartOfSpeechTag> precedingTwo = new ArrayList<PartOfSpeechTag>();
                            precedingTwo.add(precedingPos);
                            precedingTwo.add(precedingPrecedingPos);
                            HashMap<List<PartOfSpeechTag>, Integer> temp = new HashMap<List<PartOfSpeechTag>, Integer>();
                            temp.put(precedingTwo,1);
                            precedingTwoPosFrequencies.put(pos,temp);
                            added = true;
                        }
                        if(!added){
                            boolean found = false;
                            for(List<PartOfSpeechTag> precedingTags : precedingTwoPosFrequencies.get(pos).keySet()){
                                if((precedingTags.get(0).name() == precedingPos.name()) && (precedingTags.get(1).name() == precedingPrecedingPos.name()))
                                    found = true;
                            }
                            if(!found && !added){
                                Map<List<PartOfSpeechTag>,Integer> temp = precedingTwoPosFrequencies.get(pos);
                                ArrayList<PartOfSpeechTag> precedingTwo = new ArrayList<PartOfSpeechTag>();
                                precedingTwo.add(precedingPos);
                                precedingTwo.add(precedingPrecedingPos);
                                temp.put(precedingTwo,1);
                                precedingTwoPosFrequencies.put(pos,temp);
                                added = true;
                            }
                            if(!added){
                                Map<List<PartOfSpeechTag>, Integer> temp = precedingTwoPosFrequencies.get(pos);
                                List<PartOfSpeechTag> listToUpdate = null;
                                for(List<PartOfSpeechTag> precedingTags : precedingTwoPosFrequencies.get(pos).keySet()){
                                    if((precedingTags.get(0).name() == precedingPos.name()) && (precedingTags.get(1).name() == precedingPrecedingPos.name()))
                                        listToUpdate = precedingTags;
                                }
                                temp.put(listToUpdate,temp.get(listToUpdate)+1);
                                precedingTwoPosFrequencies.put(pos,temp);
                            }
                        }
                    }
				}
                precedingPrecedingPos = precedingPos;
				precedingPos = pos;
			}
		}
	}

	private PartOfSpeechTag findMatchingPartOfSpeechTag(String tag){

		for(PartOfSpeechTag pos: PartOfSpeechTag.values()){
			if(pos.name().equals(tag.toUpperCase()))
				return pos;
		}
		return null;
	}
}
