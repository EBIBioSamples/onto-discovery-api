package uk.ac.ebi.onto_discovery.api;

import java.util.List;

/**
 * A filter for {@link OntologyTermDiscoverer}, which allows to wrap discoverer calls, before forwarding them to an 
 * underlining base.   
 *
 * @author brandizi
 * <dl><dt>Date:</dt><dd>26 Jan 2016</dd></dl>
 *
 */
public class OntoTermDiscovererFilter extends OntologyTermDiscoverer
{
	protected OntologyTermDiscoverer base;

	public OntoTermDiscovererFilter ( OntologyTermDiscoverer base )
	{
		super ();
		this.base = base;
	}

	@Override
	public List<DiscoveredTerm> getOntologyTerms ( String valueLabel, String typeLabel ) throws OntologyDiscoveryException
	{
		return base.getOntologyTerms ( valueLabel, typeLabel );
	}
	
}
