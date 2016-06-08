package es.uvigo.ei.sing.io;

import java.util.Collections;
import java.util.List;

public class TocItem {

	private List<TocItem> items = Collections.emptyList();
	private String text;
	private String target;

	public TocItem(String text, String target) {
		this.text = text;
		this.target = target;
	}
	public TocItem(String text, List<TocItem> items) {
		this(text, "", items);
	}
	
	public TocItem(String text, String target, List<TocItem> items) {
		this.text = text;
		this.target = target;
		this.items = items;
	}

	
	public String getText() {
		return text;
	}
	
	public String getTarget() {
		return target;
	}

	public List<TocItem> getItems() {
		return this.items;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(!(obj instanceof TocItem)) {
			return false;
		}
		TocItem aTOCItem = (TocItem) obj;
		return 	getText().equals(aTOCItem.getText()) &&
				getTarget().equals(aTOCItem.getTarget()) &&
				getItems().equals(aTOCItem.getItems());
	}
	
	@Override
	public int hashCode() {
		int result = 33;
		result = 31 * result + getText().hashCode();
		result = 31 * result + getTarget().hashCode();
		for (TocItem item : getItems()) {
			result = 31 * result + item.hashCode();
		}
		return result;
	}
	
	@Override
	public String toString() {
		return "[TOCItem] Text: " + getText() + "; Target: " + getTarget();
	}
	
	public String getAnchor() {
		return getTarget().toLowerCase();
	}

}
