package uk.ac.ebi.onto_discovery.api;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static uk.ac.ebi.onto_discovery.api.CachedOntoTermDiscoverer.NULL_RESULT;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.onto_discovery.api.OntologyTermDiscoverer.DiscoveredTerm;
import uk.ac.ebi.utils.memory.SimpleCache;
import uk.ac.ebi.utils.time.XStopWatch;

/**
 * Do some testing of caching mechanisms.
 * TODO: more case needed (null result).
 *
 * @author brandizi
 * <dl><dt>Date:</dt><dd>18 Sep 2015</dd>
 *
 */
public class CacheTest
{
	private Logger log = LoggerFactory.getLogger ( this.getClass () );

	private static class MockUpOntoTermDiscoverer extends OntologyTermDiscoverer
	{

		@Override
		public List<DiscoveredTerm> getOntologyTerms ( String valueLabel,
				String typeLabel ) throws OntologyDiscoveryException
		{
			if ( !"homo sapiens".equalsIgnoreCase ( valueLabel ) ) return NULL_RESULT;
			List<DiscoveredTerm> result = Collections.singletonList ( new DiscoveredTerm ( 
				"http://purl.obolibrary.org/obo/NCBITaxon_9605", 100d, "Homo Sapiens" 
			));
			
			// Simulate computation time
			try {
				Thread.sleep ( 500 );
			} 
			catch ( InterruptedException ex ) {
				throw new RuntimeException ( "Internal error: " + ex.getMessage (), ex );
			}

			return result;
		}
		
	}
	
	
	/**
	 * Uses a {@link MockUpOntoTermDiscoverer} to check that results goes into the cache and that multiple calls
	 * are speeded up.
	 * 
	 */
	@Test
	public void testCache()
	{
		XStopWatch timer = new XStopWatch ();
		
		Map<String, List<DiscoveredTerm>> baseCache = new SimpleCache<> ( 1000 );
		OntologyTermDiscoverer client = new CachedOntoTermDiscoverer ( 
			new MockUpOntoTermDiscoverer (), new OntoTermDiscoveryMemCache ( baseCache )
		);
		
		timer.start ();
		List<DiscoveredTerm> terms = client.getOntologyTerms ( "homo sapiens", "organism" );
		long time1 = timer.getTime ();
				
		assertEquals ( "entry not saved in the cache!", terms, baseCache.get ( "organism:homo sapiens" ) );
		
		timer.reset ();
		timer.start ();
		for ( int i = 0; i < 100; i++ )
		{
			terms = client.getOntologyTerms ( "homo sapiens", "organism" );
			log.trace ( "Call {}, time {}", i, timer.getTime () );
		}
		timer.stop ();
		
		double time2 = timer.getTime () / 100.0;
		
		log.info ( "Second-call versus first-call time: {}, {}", time2, time1 );
		assertTrue ( "WTH?! Second call time bigger than first!", time2 < time1 );
	}
}
