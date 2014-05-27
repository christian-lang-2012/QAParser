package Models;

/**
 * Created with IntelliJ IDEA.
 * User: jacobdaniel
 * Date: 4/30/14
 * Time: 2:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class Tree<T> {
	private Node<T> root;

	public Tree(T rootData){
		root = new Node<T>(rootData);
	}

	public Node<T> getRoot(){
		return root;
	}
}
