package PhraseChunking;

import Models.*;

import java.util.*;
import java.util.regex.*;

/**
 * Created with IntelliJ IDEA.
 * User: jacobdaniel
 * Date: 5/8/14
 * Time: 1:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class PhraseChunker {
    private List<Tree<PhraseData>> phraseTrees;

    public PhraseChunker(List<WordTagPair> taggedWords){
        phraseTrees = new ArrayList<Tree<PhraseData>>();
        for(WordTagPair wtp : taggedWords){
            PhraseData data = new PhraseData("<"+wtp.getTag().name()+">",wtp.getWord());
            Tree<PhraseData> phraseTree = new Tree<PhraseData>(data);
            phraseTrees.add(phraseTree);
        }

        generateParseTree();
    }

    public List<String> getPhrases(){
        ArrayList<String> phrases = new ArrayList<String>();
        for(Tree<PhraseData> tree : phraseTrees){
            Node<PhraseData> root = tree.getRoot();
            List<Node<PhraseData>> nodes = new ArrayList<Node<PhraseData>>();
            nodes.add(root);

            while(nodes.size() > 0){
                Node<PhraseData> next = nodes.get(0);
                if(next.getData().getRegex().equals("~S~"))
                    addUniqueElement(phrases,getWordsInNode(next));
                removeNodeAndAddChildren(nodes,next);
            }
        }

        return phrases;
    }

    public List<String> getNounPhrases(){
        ArrayList<String> np = new ArrayList<String>();
        for(Tree<PhraseData> tree : phraseTrees){
            Node<PhraseData> root = tree.getRoot();
            List<Node<PhraseData>> nodes = new ArrayList<Node<PhraseData>>();
            nodes.add(root);

            while(nodes.size() > 0){
                Node<PhraseData> next = nodes.get(0);
                if(next.getData().getRegex().equals("~NP~"))
                    addUniqueElement(np,getWordsInNode(next));
                removeNodeAndAddChildren(nodes,next);
            }
        }

        return np;
    }

    public List<String> getVerbPhrases(){
        ArrayList<String> vp = new ArrayList<String>();
        for(Tree<PhraseData> tree : phraseTrees){
            Node<PhraseData> root = tree.getRoot();
            List<Node<PhraseData>> nodes = new ArrayList<Node<PhraseData>>();
            nodes.add(root);

            while(nodes.size() > 0){
                Node<PhraseData> next = nodes.get(0);
                if(next.getData().getRegex().equals("~VP~"))
                    addUniqueElement(vp,getWordsInNode(next));
                removeNodeAndAddChildren(nodes,next);
            }
        }

        return vp;
    }

    private void addUniqueElement(List<String> list, String element){
        boolean exists = false;
        int indexToRemove = -1;

        for(String item : list)
            if(item.contains(element))
                exists = true;
            else if(element.contains(item))
                indexToRemove = list.indexOf(item);

        if(!exists && indexToRemove == -1)
            list.add(element);
        else if(indexToRemove != -1){
            list.remove(indexToRemove);
            list.add(indexToRemove,element);
        }
    }

    private void removeNodeAndAddChildren(List<Node<PhraseData>> list, Node<PhraseData> node){
        list.remove(node);
        for(Node<PhraseData> child : node.getChildren())
            list.add(child);
    }
    private String getWordsInNode(Node<PhraseData> node){
        String word = "";
        if(node.getData().getWord() != null)
            word = node.getData().getWord();
        else
            for(Node<PhraseData> child : node.getChildren())  word += getWordsInNode(child)+" ";
        return word.trim();
    }

    private void generateParseTree(){
        int iterations = 0;
        while(phraseTrees.size() > 1 && iterations < 5){
            boolean found;
            iterations++;
            do{
                found = findAdjectivePhrases();
            }while(found);

            do{
                found = findAdverbPhrases();
            }while(found);

            do{
                found = findNounPhrases();
            }while(found);

            do{
                found = findPrepositionalPhrases();
            }while(found);

            do{
                found = findVerbPhrases();
            }while(found);

            do{
                found = findSentences();
            }while(found);
        }
    }

    private String getTreeRegex(List<Tree<PhraseData>> phraseTrees){
        String sentence = "";
        for(Tree<PhraseData> phraseTree : phraseTrees){
            sentence += phraseTree.getRoot().getData().getRegex();
        }
        return sentence;
    }

    private List<Tree<PhraseData>> getTreesToCombine(String regex){
        List<Tree<PhraseData>> treesToCombine = new ArrayList<Tree<PhraseData>>();
        for(Tree<PhraseData> phraseTree : phraseTrees){
            treesToCombine.add(phraseTree);
            String curRegex = getTreeRegex(treesToCombine);
            if(curRegex.contains(regex)){
                Tree<PhraseData> tree = null;
                while(curRegex.contains(regex)){
                    tree = treesToCombine.remove(0);
                    curRegex = getTreeRegex(treesToCombine);
                }
                treesToCombine.add(0,tree);
                break;
            }
        }
        return treesToCombine;
    }

    private void combineTrees(List<Tree<PhraseData>> trees, String regex){
        PhraseData data = new PhraseData(regex);
        Tree<PhraseData> tree = new Tree<PhraseData>(data);
        Node<PhraseData> root = tree.getRoot();

        for(Tree<PhraseData> phraseTree : trees){
            root.add(phraseTree.getRoot());
        }
        phraseTrees.add(phraseTrees.indexOf(trees.get(0)), tree);

        for(Tree<PhraseData> phraseTree : trees){
            phraseTrees.remove(phraseTree);
        }
    }

    private boolean findNounPhrases(){
        boolean npFound = false;
        for(String pattern : NPGrammar.getPatterns()){
            String sentence = getTreeRegex(phraseTrees);
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(sentence);
            while(m.find()) {
                List<Tree<PhraseData>> trees = getTreesToCombine(m.group());
                combineTrees(trees,"~NP~");
                npFound = true;
            }
        }
        return npFound;
    }

    private boolean findVerbPhrases(){
        boolean vpFound = false;
        for(String pattern : VPGrammar.getPatterns()){
            String sentence = getTreeRegex(phraseTrees);
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(sentence);
            while(m.find()) {
                List<Tree<PhraseData>> trees = getTreesToCombine(m.group());
                combineTrees(trees,"~VP~");
                vpFound = true;
            }
        }
        return vpFound;
    }

    private boolean findAdjectivePhrases(){
        boolean adjpFound = false;
        for(String pattern : ADJPGrammar.getPatterns()){
            String sentence = getTreeRegex(phraseTrees);
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(sentence);
            while(m.find()) {
                List<Tree<PhraseData>> trees = getTreesToCombine(m.group());
                combineTrees(trees,"~ADJP~");
                adjpFound = true;
            }
        }
        return adjpFound;
    }

    private boolean findAdverbPhrases(){
        boolean advpFound = false;
        for(String pattern : ADVPGrammar.getPatterns()){
            String sentence = getTreeRegex(phraseTrees);
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(sentence);
            while(m.find()) {
                List<Tree<PhraseData>> trees = getTreesToCombine(m.group());
                combineTrees(trees,"~ADVP~");
                advpFound = true;
            }
        }
        return advpFound;
    }

    private boolean findPrepositionalPhrases(){
        boolean ppFound = false;
        for(String pattern : PPGrammar.getPatterns()){
            String sentence = getTreeRegex(phraseTrees);
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(sentence);
            while(m.find()) {
                List<Tree<PhraseData>> trees = getTreesToCombine(m.group());
                combineTrees(trees,"~PP~");
                ppFound = true;
            }
        }
        return ppFound;
    }

    private boolean findSentences(){
        boolean sentenceFound = false;
        for(String pattern : SentenceGrammar.getPatterns()){
            String sentence = getTreeRegex(phraseTrees);
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(sentence);
            while(m.find()) {
                List<Tree<PhraseData>> trees = getTreesToCombine(m.group());
                combineTrees(trees,"~S~");
                sentenceFound = true;
            }
        }
        return sentenceFound;
    }
}
