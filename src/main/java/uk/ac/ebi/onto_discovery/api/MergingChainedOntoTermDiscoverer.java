package uk.ac.ebi.onto_discovery.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * An advanced version of {@link ChainedOntoTermDiscoverer}, which factorise on duplicated results.
 *
 * @author brandizi
 * <dl><dt>Date:</dt><dd>8 Dec 2015</dd></dl>
 *
 */
public class MergingChainedOntoTermDiscoverer extends ChainedOntoTermDiscoverer
{

	public MergingChainedOntoTermDiscoverer ( List<OntologyTermDiscoverer> discoverers )
	{
		super ( discoverers );
	}
	
	
	/**
	 * Invokes the discoverers in passed by the constructor, in the order they are listed, and ignoring the terms that 
	 * have already been found (so, discoverers on top of the list have priority).
	 *   
	 */
	@Override
	public List<DiscoveredTerm> getOntologyTerms ( String valueLabel, String typeLabel ) throws OntologyDiscoveryException
	{
		if ( this.discoverers == null || this.discoverers.isEmpty () ) return null;
		
		List<DiscoveredTerm> results = new ArrayList<OntologyTermDiscoverer.DiscoveredTerm> ();
		Set<String> alreadySeenIris = new HashSet<> ();
		
		boolean allNull = true;
		for ( OntologyTermDiscoverer discoverer: this.discoverers )
		{
			List<DiscoveredTerm> thisResults = discoverer.getOntologyTerms ( valueLabel, typeLabel );
			if ( thisResults == null ) continue;
			
			allNull = false;
			
			for ( DiscoveredTerm dterm: thisResults )
			{
				String iri = dterm.getIri ();
				if ( alreadySeenIris.contains ( iri ) ) continue;
				results.add ( dterm );
				alreadySeenIris.add ( iri );
			}
		}
		
		return allNull ? null : results;
	}
	
}
