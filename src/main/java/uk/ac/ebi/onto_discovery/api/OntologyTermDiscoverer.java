package uk.ac.ebi.onto_discovery.api;

import java.util.List;

/**
 * A generic interface for representing a service that is able to find the URI of an OWL class which of a value/type
 * label strings are assumed to represent an instance. Specific implementations use services like ZOOMA or Bioportal
 * Annotator for that.
 *
 * <dl><dt>date</dt><dd>May 27, 2013</dd></dl>
 * @author Marco Brandizi
 *
 */
public class OntologyTermDiscoverer
{
	/**
	 * Used to represent a discovered result.
	 *
	 * <dl><dt>date</dt><dd>8 Aug 2014</dd></dl>
	 * @author Marco Brandizi
	 *
	 */
	public static class DiscoveredTerm
	{
		private String iri;
		private Double score;
		private String label;
		private String provenance;
		
		
		public DiscoveredTerm ( String iri, Double score, String label, String provenace )
		{
			super ();
			this.iri = iri;
			this.score = score;
			this.label = label;
			this.provenance = provenace;
		}

		public DiscoveredTerm ( String iri, Double score, String label )
		{
			this ( iri, score, label, null );
		}

		
		public DiscoveredTerm ( String iri, Double score )
		{
			this ( iri, score, null );
		}
				
		
		public String getIri ()
		{
			return iri;
		}

		public Double getScore ()
		{
			return score;
		}
		
		public String getLabel ()
		{
			return label;
		}
		
		public String getProvenance ()
		{
			return provenance;
		}

		@Override
		public String toString () {
			return String.format ( "<%s> '%s' (%f, prov: '%s')", getIri (), getLabel (), getScore (), getProvenance () );
		}

	}
	
	
	/**
	 * <p>Returns a score-ordered list of {@link DiscoveredTerm terms} associated to the parameters.</p>
	 * 
	 * <p>Should return an empty list if no sensible URI was found for the parameters 
	 * (or if the parameters are null or invalid)</p>
	 * 
	 * <p>Should return null if you don't want to save any result (eg, in a cache) for this parameter pair, for instance
	 * because you want to re-attempt a search in case the same parmeter pair comes in again.</p>
	 *  
	 * <p>The default implementation returns null.</p>
	 */
	public List<DiscoveredTerm> getOntologyTerms ( String valueLabel, String typeLabel ) throws OntologyDiscoveryException
	{
		return null;
	}
	
}
