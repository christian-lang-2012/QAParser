package PhraseChunking;

import Models.*;

import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jacobdaniel
 * Date: 5/5/14
 * Time: 1:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class NPTable {
	private List<NounPhrase> sampleNounPhrases;

	public NPTable(boolean readTreebank){
		sampleNounPhrases = new ArrayList<NounPhrase>();

		if(readTreebank){
			getDataFromTreebank();
			printToFile();
		}
		else getDataFromFile();
	}

	private void getDataFromTreebank(){
		File rootDir = new File("Data/Penn Treebank Samples");
		File[] files = rootDir.listFiles();

		for(File file: files){
			readFile(file);
		}
	}

	private void readFile(File file){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			try {
				String line = reader.readLine();
				while(line != null){
					if(line.startsWith("[") && line.endsWith("]")){
						line = line.replace("[","").replace("]","").trim();
						if(line.endsWith("VBN"))
							System.out.println(line);
						NounPhrase nounPhrase = parseNounPhrase(line);
						if(nounPhrase != null && !sampleNounPhrases.contains(nounPhrase))
							sampleNounPhrases.add(nounPhrase);
					}
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

	private void getDataFromFile(){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File("Data/NPTableData/SampleNounPhrases.txt")));
			try {
				String line = reader.readLine();
				while(line != null){
					NounPhrase nounPhrase = new NounPhrase();
					String[] tags = line.trim().split(" ");
					for(String tagName : tags){
						PartOfSpeechTag tag = findMatchingPartOfSpeechTag(tagName);
						nounPhrase.addTag(tag);
					}
					sampleNounPhrases.add(nounPhrase);
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

	private void printToFile(){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("Data/NPTableData/SampleNounPhrases.txt")));
			for(NounPhrase nounPhrase : sampleNounPhrases){
				for(int i = 0; i < nounPhrase.getNumTags(); i++){
					writer.write(nounPhrase.getTagAtIndex(i).name() + " ");
				}
				writer.write("\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private NounPhrase parseNounPhrase(String phrase){
		NounPhrase nounPhrase = new NounPhrase();
		String[] taggedWords = phrase.split(" +");
		for(String word : taggedWords){
			String tagName = word.split("/")[1];
			PartOfSpeechTag tag = findMatchingPartOfSpeechTag(tagName);
			if(tag == null)
				return null;
			nounPhrase.addTag(tag);
		}
		return nounPhrase;
	}

	private PartOfSpeechTag findMatchingPartOfSpeechTag(String tag){

		for(PartOfSpeechTag pos: PartOfSpeechTag.values()){
			if(pos.name().equals(tag.toUpperCase()))
				return pos;
		}
		return null;
	}

	public List<NounPhrase> getSampleNounPhrases(){
		return sampleNounPhrases;
	}
}
