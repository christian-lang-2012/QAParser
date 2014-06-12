package PoSTagging;

import Models.*;

import javax.swing.*;
import java.util.*;


/**
 * Created with IntelliJ IDEA.
 * User: jacobdaniel
 * Date: 4/29/14
 * Time: 3:25 PM
 * PartOfSpeechTagger parses a sentence and tags each word using stochastic tagging
 */
public class PartOfSpeechTagger {
	private String[] words;
	private Tree<TagProbability> tagProbabilityTree;
	private TagTable tagTable;
	private final int K = 2;//K represents how many children can be added in A-Star method
	private List<WordTagPair> taggedWords;

  	public PartOfSpeechTagger(){
	  	tagProbabilityTree = new Tree<TagProbability>(new TagProbability(null,1.0f,null));
		tagTable = new TagTable(false);
	}

	public List<WordTagPair> getTaggedWords(){
		return taggedWords;
	}

    /**
     * Parse a sentence, generate tree representing all possible tag combinations,
     * pick the best sequence based on probability
     * @param sentence
     */
	public void parseSentence(String sentence){
	 	sentence = sentence.replace("!","").replace("?","").replace(".","").replace(",","")
                 .replace("-","").replace("(","").replace(")","").replace("/"," ").trim().toLowerCase();
        sentence = sentence.replaceAll("\\s{2,}", " ");
		words = sentence.split(" ");

		ArrayList<String> wordsList = new ArrayList<String>();
		for(String word : words){
			wordsList.add(word);
		}

		ArrayList<Node<TagProbability>> leaves = new ArrayList<Node<TagProbability>>();
		leaves.add(tagProbabilityTree.getRoot());
		populateTree(leaves,wordsList);
		findMostProbableTagSequenceAStar();
        //findMostProbableTagSequenceExhaustive();
	}

    /**
     * Populate tree with all possible tag sequences
     * @param leaves
     * @param words
     */
	private void populateTree(List<Node<TagProbability>> leaves, List<String> words){
		Node<TagProbability> parent = leaves.get(0).getParent();
		List<TagProbability> tagProbabilities;
		if(parent != null) {
			for(Node<TagProbability> leaf : leaves){
                //Approximation: Only consider the top half most likely tags to improve performance
				tagProbabilities = getTopHalfProbs(calculatePossibleTagProbabilities(words.get(0), leaf.getData().getTag(), parent.getData().getTag()));
                for(TagProbability tp : tagProbabilities){
                    leaf.add(new Node<TagProbability>(tp));
				}
			}
		}

		else{
			tagProbabilities = calculatePossibleTagProbabilities(words.get(0), null, null);
			for(TagProbability tp : tagProbabilities){
                leaves.get(0).add(new Node<TagProbability>(tp));
			}
		}

		words.remove(0);

		if(words.size() > 0){
			for(Node<TagProbability> leaf : leaves){
				ArrayList<String> wordsCopy = new ArrayList<String>();
				for(String word : words)
					wordsCopy.add(word);
                if(leaf.getChildren().size() > 0){
                    //Recurse on children
				    populateTree(leaf.getChildren(),wordsCopy);
                }
			}
		}
	}

    /**
     * Takes a list of tag probilities and returns the top half
     * @param probs
     * @return
     */
    private List<TagProbability> getTopHalfProbs(List<TagProbability> probs){
        List<TagProbability> topHalf = new ArrayList<TagProbability>();
        for(TagProbability tp : probs){
            if(topHalf.size() < Math.max((probs.size()/2+1),3))
                topHalf.add(tp);
            else{
                TagProbability smallest = getSmallestTagProbability(topHalf);
                if(tp.getProbability() > smallest.getProbability()){
                    topHalf.remove(smallest);
                    topHalf.add(tp);
                }
            }
        }
        return topHalf;
    }

    private TagProbability getSmallestTagProbability(List<TagProbability> probs){
        TagProbability smallest = probs.get(0);
        for(TagProbability tp : probs){
            if(tp.getProbability() < smallest.getProbability())
                smallest = tp;
        }
        return smallest;
    }

    /**
     * Takes a word and returns the most likely tags based on word ending
     * @param word
     * @return
     */
    private Set<PartOfSpeechTag> analyzeWordForPossibleTags(String word){
        Map<PartOfSpeechTag, Integer> tagCount = new HashMap<PartOfSpeechTag, Integer>();
        Set<PartOfSpeechTag> mostLikelyTags = new HashSet<PartOfSpeechTag>();
        if(isNumeric(word)){
            mostLikelyTags.add(PartOfSpeechTag.CD);
        }
        else if(word.length() > 1){
            //Consider last two letters
            String ending = word.substring(word.length()-2);
            for(String taggedWord : tagTable.getAllWords()){
                if(taggedWord.endsWith(ending)){
                    for(PartOfSpeechTag tag : tagTable.getWordTags(taggedWord)){
                        if(!tagCount.containsKey(tag)) //if a word has same ending it may be correct tag
                            tagCount.put(tag,1);
                        else tagCount.put(tag,tagCount.get(tag)+1);
                    }
                }
            }

            int numTags = tagCount.size();
            //pick the top 4 tags based on frequency
            for(int i = 0; i < Math.min(numTags,4); i++){
                PartOfSpeechTag max = getMaxTag(tagCount);
                mostLikelyTags.add(max);
                tagCount.remove(max);
            }
        }
        return mostLikelyTags;
    }

    private boolean isNumeric(String word){
        return word.matches("-?\\d+(\\.\\d+)?");
    }

    private PartOfSpeechTag getMaxTag(Map<PartOfSpeechTag,Integer> tagCount){
        int max = 0;
        PartOfSpeechTag maxTag = null;
        for(PartOfSpeechTag tag : tagCount.keySet()){
            if(tagCount.get(tag).intValue() > max){
                max = tagCount.get(tag).intValue();
                maxTag = tag;
            }
        }
        return maxTag;
    }

    /**
     * Finds the probability of all possible tags for a word based on previous two tags
     * and Corpus frequencies
     * @param word
     * @param previous
     * @param previousPrevious
     * @return
     */
	private List<TagProbability> calculatePossibleTagProbabilities(String word, PartOfSpeechTag previous, PartOfSpeechTag previousPrevious){
		ArrayList<TagProbability> tagProbabilities = new ArrayList<TagProbability>();
		Set<PartOfSpeechTag> possibleTags = new HashSet<PartOfSpeechTag>();

		if(tagTable.isWordInTable(word)){
            //the word is known, get all its possible tags from tag table
			for(PartOfSpeechTag tag : tagTable.getWordTags(word))
				possibleTags.add(tag);
		}

        if(possibleTags.isEmpty()){
            //the word is unknown, get all its possible tags based on ending
            possibleTags = analyzeWordForPossibleTags(word);
        }

        if(possibleTags.isEmpty()){
            //the word is unknown and its ending could not be matched, consider it a noun, verb, adverb, or adjective
            possibleTags.add(PartOfSpeechTag.NN);
            possibleTags.add(PartOfSpeechTag.VB);
            possibleTags.add(PartOfSpeechTag.JJ);
            possibleTags.add(PartOfSpeechTag.RB);
        }


		for(PartOfSpeechTag tag : possibleTags){
            /**
             * Probability is represented as two separate probabilities. TagHistoryProbability is the probability that
             * a sequence of tags shows up in the table. Ex) Probability that the current word is a noun given that the
             * previous two words were verb noun. LexicalProbability is the probability that the current word is a given
             * part of speech. Ex) Probability that race is a noun.
             */
			float tagHistoryProbability;
			float lexicalProbability;
			if(previous == null)
                //No previous tags to reference, guess tag based on tag frequency in table
				tagHistoryProbability = 1.0f * tagTable.getTagFrequency(tag) / tagTable.getTotalFrequency();
			else if(previousPrevious != null){
                if(tagTable.isTagPrecededByTags(previousPrevious,previous,tag))
                    //+1 and +36 are there for smoothing
                    tagHistoryProbability = 1.0f * (tagTable.getPrecedingTagsFrequency(previousPrevious,previous,tag) +1)/ (tagTable.getPrecedingTagFrequency(previousPrevious,previous)*tagTable.getTagFrequency(tag)+36);
                else tagHistoryProbability = 1.0f / (tagTable.getPrecedingTagFrequency(previousPrevious,previous)*tagTable.getTagFrequency(tag)+36);
            }
            else if(tagTable.isTagPrecededByTag(previous,tag))
                tagHistoryProbability = 1.0f * (tagTable.getPrecedingTagFrequency(previous,tag) +1)/ (tagTable.getTagFrequency(previous)*tagTable.getTagFrequency(tag)+36);
            else tagHistoryProbability = 1.0f / (tagTable.getTagFrequency(previous)*tagTable.getTagFrequency(tag)+36);

			if(tagTable.isWordInTable(word) && tagTable.isWordTagPairInTable(word,tag))
				lexicalProbability = 1.0f * (tagTable.getWordTagFrequency(word,tag) +1)/ (tagTable.getTagFrequency(tag)+tagTable.getTotalWords());
            else if(tagTable.isWordInTable(word))
			    lexicalProbability = 1.0f / (tagTable.getTagFrequency(tag)+tagTable.getTotalWords());
            else lexicalProbability = 1.0f / (tagTable.getTagFrequency(tag)+tagTable.getTotalWords());

            TagProbability tp = new TagProbability(tag,tagHistoryProbability*lexicalProbability,word);
            tagProbabilities.add(tp);
		}

		return tagProbabilities;
	}

	private void findMostProbableTagSequenceExhaustive(){
		Node<TagProbability> next = tagProbabilityTree.getRoot();
		List<Node<TagProbability>> nodes = new ArrayList<Node<TagProbability>>();
		nodes.add(tagProbabilityTree.getRoot());

		do{
			removeNodeAndAddChildrenToList(next, nodes);
			next = getNodeWithMaxProbability(nodes);
		} while(!next.getData().getWord().equals(words[words.length-1]) || next.getChildren().size() > 0);

		ArrayList<TagProbability> results = new ArrayList<TagProbability>();

		while(next.getParent() != null){
			results.add(0,next.getData());
			next = next.getParent();
		}

		taggedWords = new ArrayList<WordTagPair>();
		for(int i = 0; i < words.length; i++){
			taggedWords.add(new WordTagPair(words[i],results.get(i).getTag()));
		}
	}

    /**
     * Find the most likely sequence of tags in tree
     */
	private void findMostProbableTagSequenceAStar(){
		Node<TagProbability> next = tagProbabilityTree.getRoot();
		List<Node<TagProbability>> nodes = new ArrayList<Node<TagProbability>>();
		nodes.add(tagProbabilityTree.getRoot());

		do{
            removeNodeAndAddBestKChildrenToList(next, nodes);
            next = getNodeWithMaxProbability(nodes);
		}while(next.getChildren().size() > 0);

		ArrayList<TagProbability> results = new ArrayList<TagProbability>();

		while(next.getParent() != null){
			results.add(0,next.getData());
			next = next.getParent();
		}

		taggedWords = new ArrayList<WordTagPair>();
		for(int i = 0; i < words.length; i++){
			taggedWords.add(new WordTagPair(words[i],results.get(i).getTag()));
		}
	}

    private void removeTerminalNodes(List<Node<TagProbability>> nodes){
        List<Node<TagProbability>> nodesToRemove = new ArrayList<Node<TagProbability>>();
        for(int i = 0; i < nodes.size(); i++){
            if(nodes.get(i).getChildren().size() == 0 && !nodes.get(i).getData().getWord().equals(words[words.length-1])){
                nodesToRemove.add(nodes.get(i));
            }
        }
        for(Node<TagProbability> node : nodesToRemove)
            nodes.remove(node);
    }

	private Node<TagProbability> getNodeWithMaxProbability(List<Node<TagProbability>> nodes){
		float maxProbability = -1f;
		Node<TagProbability> maxNode = null;

		for(Node<TagProbability> node : nodes){
			if(Math.abs(node.getData().getProbability()) > maxProbability){
				maxProbability = node.getData().getProbability();
				maxNode = node;
			}
		}

		return maxNode;
	}

    /**
     * Remove node from list and add all children
     * @param node
     * @param list
     */
	private void removeNodeAndAddChildrenToList(Node<TagProbability> node, List<Node<TagProbability>> list){
		List<Node<TagProbability>> children = node.getChildren();
		list.remove(node);
		for(Node<TagProbability> child : children){
			Node<TagProbability> newChild = child;
			newChild.setData(new TagProbability(child.getData().getTag(), node.getData().getProbability() *
				child.getData().getProbability(),child.getData().getWord()));
			list.add(newChild);
		}
	}

    /**
     * Remove node from list and add K best children
     * @param node
     * @param list
     */
	private void removeNodeAndAddBestKChildrenToList(Node<TagProbability> node, List<Node<TagProbability>> list){
		List<Node<TagProbability>> children = node.getChildren();
		list.remove(node);
        int size = children.size();
		for(int i = 0; i < Math.min(K,size); i++){
			Node<TagProbability> bestChild = getNodeWithMaxProbability(children);
			Node<TagProbability> newChild = bestChild;
			newChild.setData(new TagProbability(bestChild.getData().getTag(), node.getData().getProbability() *
				bestChild.getData().getProbability(),bestChild.getData().getWord()));
			list.add(newChild);
			children.remove(bestChild);
		}
	}
}
