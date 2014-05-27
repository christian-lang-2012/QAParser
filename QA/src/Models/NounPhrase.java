package Models;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jacobdaniel
 * Date: 5/5/14
 * Time: 1:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class NounPhrase {
	private List<PartOfSpeechTag> tags;

	public NounPhrase(){
		tags = new ArrayList<PartOfSpeechTag>();
	}

	public NounPhrase(List<PartOfSpeechTag> tags){
		this.tags = tags;
	}

	public void addTag(PartOfSpeechTag tag){
		tags.add(tag);
	}

	public void removeTag(PartOfSpeechTag tag){
		tags.remove(tag);
	}

	public PartOfSpeechTag getTagAtIndex(int index){
		return tags.get(index);
	}

	public int getNumTags(){
		return tags.size();
	}

	@Override
	public String toString(){
		String result = "";
		for(PartOfSpeechTag tag : tags){
			result += tag.name() + " ";
		}
		return result.trim();
	}

	@Override
	public boolean equals(Object other){
		if(other == null)
			return false;
		if(!(other instanceof  NounPhrase))
			return false;
		NounPhrase np = (NounPhrase)other;
		if(np.getNumTags() != this.getNumTags())
			return false;
		for(int i = 0; i < this.getNumTags(); i++){
			if(!this.getTagAtIndex(i).equals(np.getTagAtIndex(i)))
				return false;
		}
		return true;
	}
}
