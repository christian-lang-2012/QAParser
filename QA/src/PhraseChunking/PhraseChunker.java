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
                    //addUniqueElement(phrases,getWordsInNode(next));
                    phrases.add(getWordsInNode(next));
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
        List<FailureTransition> failures = new ArrayList<FailureTransition>();
        Stack<List<Tree<PhraseData>>> allChanges = new Stack<List<Tree<PhraseData>>>();
        boolean changeMade;
        int iterations = 0;
        while(phraseTrees.size() > 1 && iterations < 300){
            changeMade = false;
            iterations++;
            Subtree<List<Tree<PhraseData>>> adjpTree = findAdjectivePhrases();
            for(List<Tree<PhraseData>> trees : adjpTree.getSubtrees()){
                if(!isKnownFailureTransition(failures, new FailureTransition(copyPhraseTrees(),trees))){
                    allChanges.push(trees);
                    combineTrees(trees, adjpTree.getType());
                    changeMade = true;
                    break;
                }
            }

            Subtree<List<Tree<PhraseData>>> advpTree = findAdverbPhrases();
            for(List<Tree<PhraseData>> trees : advpTree.getSubtrees()){
                if(!isKnownFailureTransition(failures, new FailureTransition(copyPhraseTrees(), trees))){
                    allChanges.push(trees);
                    combineTrees(trees, advpTree.getType());
                    changeMade = true;
                    break;
                }
            }

            Subtree<List<Tree<PhraseData>>> npTree = findNounPhrases();
            for(List<Tree<PhraseData>> trees : npTree.getSubtrees()){
                if(!isKnownFailureTransition(failures, new FailureTransition(copyPhraseTrees(), trees))){
                    allChanges.push(trees);
                    combineTrees(trees, npTree.getType());
                    changeMade = true;
                    break;
                }
            }

            Subtree<List<Tree<PhraseData>>> ppTree = findPrepositionalPhrases();
            for(List<Tree<PhraseData>> trees : ppTree.getSubtrees()){
                if(!isKnownFailureTransition(failures, new FailureTransition(copyPhraseTrees(), trees))){
                    allChanges.push(trees);
                    combineTrees(trees, ppTree.getType());
                    changeMade = true;
                    break;
                }
            }

            Subtree<List<Tree<PhraseData>>> vpTree = findVerbPhrases();
            for(List<Tree<PhraseData>> trees : vpTree.getSubtrees()){
                if(!isKnownFailureTransition(failures, new FailureTransition(copyPhraseTrees(), trees))){
                    allChanges.push(trees);
                    combineTrees(trees, vpTree.getType());
                    changeMade = true;
                    break;
                }
            }

            Subtree<List<Tree<PhraseData>>> sTree = findSentences();
            for(List<Tree<PhraseData>> trees : sTree.getSubtrees()){
                if(!isKnownFailureTransition(failures, new FailureTransition(copyPhraseTrees(), trees))){
                    allChanges.push(trees);
                    combineTrees(trees, sTree.getType());
                    changeMade = true;
                    break;
                }
            }

            if(!changeMade){
                List<Tree<PhraseData>> latestChange = allChanges.pop();
                unCombineTrees(latestChange);
                FailureTransition failure = new FailureTransition(copyPhraseTrees(),latestChange);
                failures.add(failure);
            }
        }
    }

    private List<Tree<PhraseData>> copyPhraseTrees(){
        List<Tree<PhraseData>> copy = new ArrayList<Tree<PhraseData>>();
        for(Tree<PhraseData> tree : phraseTrees){
            Tree<PhraseData> newTree = new Tree<PhraseData>(tree.getRoot().getData());
            copy.add(newTree);
        }
        return copy;
    }

    private String getTreeRegex(List<Tree<PhraseData>> phraseTrees){
        String sentence = "";
        for(Tree<PhraseData> phraseTree : phraseTrees){
            sentence += phraseTree.getRoot().getData().getRegex();
        }
        return sentence;
    }

    private boolean isKnownFailureTransition(List<FailureTransition> failures, FailureTransition failure){
        boolean isFailure = false;

        for(FailureTransition ft : failures){
            if(ft.equals(failure))
                isFailure = true;
        }

        return isFailure;
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

    private void unCombineTrees(List<Tree<PhraseData>> trees){
        Tree<PhraseData> child = trees.get(0);
        int indexToRemove = -1;
        for(Tree<PhraseData> tree : phraseTrees){
            if(tree.getRoot().getChildren().contains(child.getRoot())){
                indexToRemove = phraseTrees.indexOf(tree);
            }
        }
        phraseTrees.remove(indexToRemove);
        for(Tree<PhraseData> tree : trees){
            phraseTrees.add(indexToRemove,tree);
            indexToRemove++;
        }
    }

    private Subtree<List<Tree<PhraseData>>> findNounPhrases(){
        Subtree<List<Tree<PhraseData>>> combos = new Subtree<List<Tree<PhraseData>>>("~NP~");
        for(String pattern : NPGrammar.getPatterns()){
            String sentence = getTreeRegex(phraseTrees);
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(sentence);
            while(m.find()) {
                List<Tree<PhraseData>> trees = getTreesToCombine(m.group());
                combos.add(trees);
            }
        }
        return combos;
    }

    private Subtree<List<Tree<PhraseData>>> findVerbPhrases(){
        Subtree<List<Tree<PhraseData>>> combos = new Subtree<List<Tree<PhraseData>>>("~VP~");
        for(String pattern : VPGrammar.getPatterns()){
            String sentence = getTreeRegex(phraseTrees);
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(sentence);
            while(m.find()) {
                List<Tree<PhraseData>> trees = getTreesToCombine(m.group());
                combos.add(trees);
            }
        }
        return combos;
    }

    private Subtree<List<Tree<PhraseData>>> findAdjectivePhrases(){
        Subtree<List<Tree<PhraseData>>> combos = new Subtree<List<Tree<PhraseData>>>("~ADJP~");
        for(String pattern : ADJPGrammar.getPatterns()){
            String sentence = getTreeRegex(phraseTrees);
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(sentence);
            while(m.find()) {
                List<Tree<PhraseData>> trees = getTreesToCombine(m.group());
                combos.add(trees);
            }
        }
        return combos;
    }

    private Subtree<List<Tree<PhraseData>>> findAdverbPhrases(){
        Subtree<List<Tree<PhraseData>>> combos = new Subtree<List<Tree<PhraseData>>>("~ADVP~");
        for(String pattern : ADVPGrammar.getPatterns()){
            String sentence = getTreeRegex(phraseTrees);
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(sentence);
            while(m.find()) {
                List<Tree<PhraseData>> trees = getTreesToCombine(m.group());
                combos.add(trees);
            }
        }
        return combos;
    }

    private Subtree<List<Tree<PhraseData>>> findPrepositionalPhrases(){
        Subtree<List<Tree<PhraseData>>> combos = new Subtree<List<Tree<PhraseData>>>("~PP~");
        for(String pattern : PPGrammar.getPatterns()){
            String sentence = getTreeRegex(phraseTrees);
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(sentence);
            while(m.find()) {
                List<Tree<PhraseData>> trees = getTreesToCombine(m.group());
                combos.add(trees);
            }
        }
        return combos;
    }

    private Subtree<List<Tree<PhraseData>>> findSentences(){
        Subtree<List<Tree<PhraseData>>> combos = new Subtree<List<Tree<PhraseData>>>("~S~");
        for(String pattern : SentenceGrammar.getPatterns()){
            String sentence = getTreeRegex(phraseTrees);
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(sentence);
            while(m.find()) {
                List<Tree<PhraseData>> trees = getTreesToCombine(m.group());
                combos.add(trees);
            }
        }
        return combos;
    }
}
