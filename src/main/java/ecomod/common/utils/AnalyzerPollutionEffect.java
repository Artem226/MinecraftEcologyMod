package ecomod.common.utils;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import ecomod.api.client.IAnalyzerPollutionEffect;
import ecomod.api.client.IAnalyzerPollutionEffect.TriggeringType;
import ecomod.api.pollution.PollutionData;
import net.minecraft.util.ResourceLocation;

public class AnalyzerPollutionEffect implements IAnalyzerPollutionEffect
{
	String id;
	String header;
	String description;
	
	String icon;
	
	TriggeringType triggering_type;
	PollutionData triggerring_pollution;
	

	
	private static Gson gson = new GsonBuilder().create();
	
	public AnalyzerPollutionEffect()
	{
		
	}
	
	public AnalyzerPollutionEffect(String id, String header, String desc, ResourceLocation icon, PollutionData triggering_pollution, IAnalyzerPollutionEffect.TriggeringType triggering_type)
	{
		this.id = id;
		this.header = header;
		this.description = desc;
		this.icon = icon == null ? IAnalyzerPollutionEffect.BLANK_ICON.toString() : icon.toString();
		this.triggerring_pollution = triggering_pollution;
		this.triggering_type = triggering_type;
	}
	
	public AnalyzerPollutionEffect(IAnalyzerPollutionEffect iape)
	{
		this(iape.getId(), iape.getHeader(), iape.getDescription(), iape.getIcon(), iape.getTriggerringPollution(), iape.getTriggeringType());
	}
	
	public static AnalyzerPollutionEffect createSimple(String id, PollutionData triggering_pollution, IAnalyzerPollutionEffect.TriggeringType triggering_type)
	{
		return new AnalyzerPollutionEffect(id, "ape.ecomod."+id+".name", "ape.ecomod."+id+".desc", new ResourceLocation("ecomod:textures/gui/analyzer/icons/"+id+".png"), triggering_pollution, triggering_type);
	}
	
	public static AnalyzerPollutionEffect createSimpleNull(String id, PollutionData triggering_pollution, IAnalyzerPollutionEffect.TriggeringType triggering_type)
	{
		return new AnalyzerPollutionEffect(id, "ape.ecomod."+id+".name", "ape.ecomod."+id+".desc", null, triggering_pollution, triggering_type);
	}
	
	@Override
	public PollutionData getTriggerringPollution()
	{
		return triggerring_pollution;
	}

	@Override
	public TriggeringType getTriggeringType() {
		return triggering_type;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getHeader() {
		return header;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public ResourceLocation getIcon() {
		if(icon == null || icon.equals(""))
			return IAnalyzerPollutionEffect.BLANK_ICON;
		
		return new ResourceLocation(icon);
	}

	public static AnalyzerPollutionEffect getAPEfromJSON(String json)
	{
		return gson.fromJson(json, AnalyzerPollutionEffect.class);
	}
	
	public static IAnalyzerPollutionEffect getIAPEfromJSON(String json)
	{
		return getAPEfromJSON(json);
	}
	
	public static String toJSON(AnalyzerPollutionEffect ape)
	{
		return gson.toJson(ape, AnalyzerPollutionEffect.class);
	}
	
	public static String toJSON(IAnalyzerPollutionEffect iape)
	{
		return gson.toJson(new AnalyzerPollutionEffect(iape), AnalyzerPollutionEffect.class);
	}
	
	static final Type typeOfSrc = new TypeToken<Collection<AnalyzerPollutionEffect>>(){}.getType();
	
	public static String collection_toJSON(Collection<AnalyzerPollutionEffect> collection)
	{
		return gson.toJson(collection, typeOfSrc);
	}
	
	public static List<AnalyzerPollutionEffect> ape_list_fromJSON(String json)
	{
		return gson.fromJson(json, typeOfSrc);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Strings.isNullOrEmpty(description)) ? 0 : description.hashCode());
		result = prime * result + ((Strings.isNullOrEmpty(header)) ? 0 : header.hashCode());
		result = prime * result + ((Strings.isNullOrEmpty(icon)) ? 0 : icon.hashCode());
		result = prime * result + ((Strings.isNullOrEmpty(id)) ? 0 : id.hashCode());
		result = prime * result + ((triggering_type == null) ? 0 : triggering_type.hashCode());
		result = prime * result + ((triggerring_pollution == null) ? 0 : triggerring_pollution.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object other)
	{
		if(other instanceof IAnalyzerPollutionEffect)
		{
			IAnalyzerPollutionEffect effect = (IAnalyzerPollutionEffect)other;

			return effect.getId().equals(id) && effect.getTriggeringType().equals(triggering_type) && effect.getTriggerringPollution().equals(triggerring_pollution) && effect.getHeader().equals(header) && effect.getDescription().equals(description) && effect.getIcon().equals(getIcon());
		}
	
		return false;
	}
}
