package uk.ac.ebi.onto_discovery.api;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.google.common.cache.CacheBuilder;

/**
 * A memory cache, based on {@link Map}.
 *
 * <dl><dt>date</dt><dd>1 Aug 2014</dd></dl>
 * @author Marco Brandizi
 *
 */
public class OntoTermDiscoveryMemCache extends OntoTermDiscoveryCache
{
	private final Map<String, List<DiscoveredTerm>> baseCache;

	public OntoTermDiscoveryMemCache ( Map<String, List<DiscoveredTerm>> baseCache )
	{
		this.baseCache = baseCache;
	}

	/**
	 * Uses {@link CacheBuilder} with a size of 500k entries and expire time of 4h.
	 */
	@SuppressWarnings ( { "unchecked", "rawtypes" } )
	public OntoTermDiscoveryMemCache ()
	{
		this ( ( (CacheBuilder) CacheBuilder.newBuilder () 
			.maximumSize ( 10 ) 
			.expireAfterWrite ( 4, TimeUnit.HOURS ) )
			.build ().asMap ()
		);
	}
	
	@Override
	public List<DiscoveredTerm> save ( String valueLabel, String typeLabel, List<DiscoveredTerm> terms )
		throws OntologyDiscoveryException
	{
		return baseCache.put ( StringUtils.trimToEmpty ( typeLabel )  + ":" + valueLabel, terms );
	}

	@Override
	public List<DiscoveredTerm> getOntologyTerms ( String valueLabel, String typeLabel ) throws OntologyDiscoveryException
	{
		return baseCache.get ( StringUtils.trimToEmpty ( typeLabel )  + ":" + valueLabel );
	}

	@Override
	public void clear ()
	{
		baseCache.clear ();
	}
	
}
