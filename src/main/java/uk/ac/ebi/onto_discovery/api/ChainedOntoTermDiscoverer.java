package uk.ac.ebi.onto_discovery.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Returns the concatenation of all the terms that were discovered in an underline arrays of discoverers.
 *
 * @author brandizi
 * <dl><dt>Date:</dt><dd>8 Dec 2015</dd></dl>
 *
 */
public class ChainedOntoTermDiscoverer extends OntologyTermDiscoverer
{
	protected List<OntologyTermDiscoverer> discoverers;

	public ChainedOntoTermDiscoverer ( List<OntologyTermDiscoverer> discoverers )
	{
		super ();
		this.discoverers = discoverers;
	}

	@Override
	public List<DiscoveredTerm> getOntologyTerms ( String valueLabel, String typeLabel ) throws OntologyDiscoveryException
	{
		if ( this.discoverers == null || this.discoverers.isEmpty () ) return null;
		
		List<DiscoveredTerm> results = new ArrayList<OntologyTermDiscoverer.DiscoveredTerm> ();
		
		boolean allNull = true;
		for ( OntologyTermDiscoverer discoverer: this.discoverers )
		{
			List<DiscoveredTerm> thisResults = discoverer.getOntologyTerms ( valueLabel, typeLabel );
			if ( thisResults == null ) continue;
			
			allNull = false;
			results.addAll ( thisResults );
		}
		
		return allNull ? null : results;
	}
}
