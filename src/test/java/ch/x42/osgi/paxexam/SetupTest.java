package ch.x42.osgi.paxexam;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.ops4j.pax.exam.CoreOptions.felix;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.ExamReactorStrategy;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.ops4j.pax.exam.spi.reactors.AllConfinedStagedReactorFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/** Use pax exam test runner */
@RunWith( JUnit4TestRunner.class )

/* Restart OSGi framework for each test (which is 
 * the default setting)
 */
@ExamReactorStrategy(AllConfinedStagedReactorFactory.class)

/** Example test that runs with pax exam, just
 *  checks that BundleContext is supplied and that
 *  we can install bundles
 */
public class SetupTest {

    public static final String MIME_BUNDLE_SN = "org.apache.sling.commons.mime";
    
    // pax exam configuration, what to provision etc.
    @Configuration
    public Option[] config()
    {
        return options(
            junitBundles(),
            felix().version("4.0.2"),
            mavenBundle("org.apache.sling", "org.apache.sling.commons.osgi", "2.1.0"),     
            mavenBundle("org.apache.sling", MIME_BUNDLE_SN, "2.1.4")     
        );
    }
    
    @Test
    public void testBundleContext( BundleContext ctx )
    {
        assertNotNull("Expecting BundleContext to be supplied", ctx);
    }
    
    @Test
    public void testBundleStarted(BundleContext ctx)
    {
        final Bundle mime = getBundle(ctx, MIME_BUNDLE_SN);
        assertNotNull("Expecting " + MIME_BUNDLE_SN + " bundle to be installed", 
                mime);
        assertEquals("Expecting " + MIME_BUNDLE_SN + " bundle to be active", 
                Bundle.ACTIVE, mime.getState());
    }
    
    private Bundle getBundle(BundleContext ctx, String symbolicName) {
        for(Bundle b : ctx.getBundles()) {
            if(MIME_BUNDLE_SN.equals(b.getSymbolicName())) {
                return b;
            }
        }
        return null;
    }
}