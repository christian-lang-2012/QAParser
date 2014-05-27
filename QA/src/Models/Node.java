package Models;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jacobdaniel
 * Date: 4/30/14
 * Time: 3:01 PM
 * To change this template use File | Settings | File Templates.
 */

public class Node<T>{
	private T data;
	private Node<T> parent;
	private List<Node<T>> children;

	public Node(){
		data = null;
		children = new ArrayList<Node<T>>();
		parent = null;
	}

	public Node(T data){
		this.data = data;
		this.children = new ArrayList<Node<T>>();
		parent = null;
	}

	public void add(Node<T> node){
		node.parent = this;
		children.add(node);
	}

    public void remove(Node<T> node){
        children.remove(node);
    }

	public List<Node<T>> getChildren(){
		return children;
	}

	public Node<T> getParent(){
		return parent;
	}

	public T getData(){
		return data;
	}

	public void setData(T data){
		this.data = data;
	}
}

