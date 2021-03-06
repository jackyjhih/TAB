package me.neznamy.tab.shared.placeholders;

import java.util.HashMap;
import java.util.Map;

import me.neznamy.tab.premium.Premium;
import me.neznamy.tab.shared.ITabPlayer;
import me.neznamy.tab.shared.Shared;

public abstract class Placeholder {

	protected int cooldown;
	protected String identifier;
	private Map<String, Object> replacements;
	
	@SuppressWarnings("unchecked")
	public Placeholder(String identifier, int cooldown) {
		this.identifier = identifier;
		this.cooldown = cooldown;
		if (Premium.is()) replacements = (Map<String, Object>) Premium.premiumconfig.get("placeholder-output-replacements." + identifier);
		if (replacements == null) replacements = new HashMap<String, Object>();
	}
	public String getIdentifier() {
		return identifier;
	}
	public String[] getChilds(){
		return new String[0];
	}
	public String set(String s, ITabPlayer p) {
		try {
			String value = getValue(p);
			if (value == null) value = "";
			if (replacements.containsKey(value)) value = replacements.get(value).toString();
			return s.replace(identifier, value);
		} catch (Throwable t) {
			return Shared.errorManager.printError(s, "An error occurred when setting placeholder " + identifier + (p == null ? "" : " for " + p.getName()), t);
		}
	}
	public abstract String getValue(ITabPlayer p);
}