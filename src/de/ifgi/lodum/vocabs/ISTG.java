/* CVS $Id: $ */
package de.ifgi.lodum.vocabs; 
import com.hp.hpl.jena.rdf.model.*;
 
/**
 * Vocabulary definitions from http://giv-heatmap.uni-muenster.de:8080/epad.ifgi.de/p/istg-labels_neu.ttl 
 * @author Auto-generated by schemagen on 20 Mai 2013 18:20 
 */
public class ISTG {
    /** <p>The RDF model that holds the vocabulary terms</p> */
    private static Model m_model = ModelFactory.createDefaultModel();
    
    /** <p>The namespace of the vocabulary as a string</p> */
    public static final String NS = "http://vocab.lodum.de/istg/";
    
    /** <p>The namespace of the vocabulary as a string</p>
     *  @see #NS */
    public static String getURI() {return NS;}
    
    /** <p>The namespace of the vocabulary as a resource</p> */
    public static final Resource NAMESPACE = m_model.createResource( NS );
    
    public static final Property Technique = m_model.createProperty( "http://vocab.lodum.de/istg/Technique" );
    
    public static final Property authorString = m_model.createProperty( "http://vocab.lodum.de/istg/authorString" );
    
    public static final Property badCondition = m_model.createProperty( "http://vocab.lodum.de/istg/badCondition" );
    
    public static final Property cartographer = m_model.createProperty( "http://vocab.lodum.de/istg/cartographer" );
    
    public static final Property cartographerString = m_model.createProperty( "http://vocab.lodum.de/istg/cartographerString" );
    
    public static final Property category = m_model.createProperty( "http://vocab.lodum.de/istg/category" );
    
    public static final Property churchBoundaries = m_model.createProperty( "http://vocab.lodum.de/istg/churchBoundaries" );
    
    public static final Property city = m_model.createProperty( "http://vocab.lodum.de/istg/city" );
    
    public static final Property cityBlockRepresentation = m_model.createProperty( "http://vocab.lodum.de/istg/cityBlockRepresentation" );
    
    public static final Property cityDistrict = m_model.createProperty( "http://vocab.lodum.de/istg/cityDistrict" );
    
    public static final Property color = m_model.createProperty( "http://vocab.lodum.de/istg/color" );
    
    public static final Property colored = m_model.createProperty( "http://vocab.lodum.de/istg/colored" );
    
    public static final Property condition = m_model.createProperty( "http://vocab.lodum.de/istg/condition" );
    
    public static final Property continent = m_model.createProperty( "http://vocab.lodum.de/istg/continent" );
    
    public static final Property country = m_model.createProperty( "http://vocab.lodum.de/istg/country" );
    
    public static final Property delivery = m_model.createProperty( "http://vocab.lodum.de/istg/delivery" );
    
    public static final Property displayedTime = m_model.createProperty( "http://vocab.lodum.de/istg/displayedTime" );
    
    public static final Property editorString = m_model.createProperty( "http://vocab.lodum.de/istg/editorString" );
    
    public static final Property editoraString = m_model.createProperty( "http://vocab.lodum.de/istg/editoraString" );
    
    public static final Property handwritingBack = m_model.createProperty( "http://vocab.lodum.de/istg/handwritingBack" );
    
    public static final Property handwritingFront = m_model.createProperty( "http://vocab.lodum.de/istg/handwritingFront" );
    
    public static final Property historicPlacename = m_model.createProperty( "http://vocab.lodum.de/istg/historicPlacename" );
    
    public static final Property historicRegion = m_model.createProperty( "http://vocab.lodum.de/istg/historicRegion" );
    
    public static final Property historicalLocation = m_model.createProperty( "http://vocab.lodum.de/istg/historicalLocation" );
    
    public static final Property houseNumbers = m_model.createProperty( "http://vocab.lodum.de/istg/houseNumbers" );
    
    public static final Property illustrationdate = m_model.createProperty( "http://vocab.lodum.de/istg/illustrationdate" );
    
    public static final Property inhaltsverzeichnis = m_model.createProperty( "http://vocab.lodum.de/istg/inhaltsverzeichnis" );
    
    public static final Property inhaltsverzeichnis_link = m_model.createProperty( "http://vocab.lodum.de/istg/inhaltsverzeichnis-link" );
    
    public static final Property inhaltsverzeichnis_text = m_model.createProperty( "http://vocab.lodum.de/istg/inhaltsverzeichnis-text" );
    
    public static final Property inventory = m_model.createProperty( "http://vocab.lodum.de/istg/inventory" );
    
    public static final Property library = m_model.createProperty( "http://vocab.lodum.de/istg/library" );
    
    public static final Property maintitle = m_model.createProperty( "http://vocab.lodum.de/istg/maintitle" );
    
    public static final Property mapScale = m_model.createProperty( "http://vocab.lodum.de/istg/mapScale" );
    
    public static final Property page = m_model.createProperty( "http://vocab.lodum.de/istg/page" );
    
    public static final Property parcelRepresentation = m_model.createProperty( "http://vocab.lodum.de/istg/parcelRepresentation" );
    
    public static final Property plz = m_model.createProperty( "http://vocab.lodum.de/istg/plz" );
    
    public static final Property politicalBoundaries = m_model.createProperty( "http://vocab.lodum.de/istg/politicalBoundaries" );
    
    public static final Property postcode = m_model.createProperty( "http://vocab.lodum.de/istg/postcode" );
    
    public static final Property printedBack = m_model.createProperty( "http://vocab.lodum.de/istg/printedBack" );
    
    public static final Property printedFront = m_model.createProperty( "http://vocab.lodum.de/istg/printedFront" );
    
    public static final Property publishedCustom = m_model.createProperty( "http://vocab.lodum.de/istg/publishedCustom" );
    
    public static final Property publishingLocation = m_model.createProperty( "http://vocab.lodum.de/istg/publishingLocation" );
    
    public static final Property publishingdate = m_model.createProperty( "http://vocab.lodum.de/istg/publishingdate" );
    
    public static final Property recording = m_model.createProperty( "http://vocab.lodum.de/istg/recording" );
    
    public static final Property recordingTime = m_model.createProperty( "http://vocab.lodum.de/istg/recordingTime" );
    
    public static final Property region = m_model.createProperty( "http://vocab.lodum.de/istg/region" );
    
    public static final Property reihe = m_model.createProperty( "http://vocab.lodum.de/istg/reihe" );
    
    public static final Property relief = m_model.createProperty( "http://vocab.lodum.de/istg/relief" );
    
    public static final Property rezension = m_model.createProperty( "http://vocab.lodum.de/istg/rezension" );
    
    public static final Property signatur = m_model.createProperty( "http://vocab.lodum.de/istg/signature" );
    
    public static final Property state = m_model.createProperty( "http://vocab.lodum.de/istg/state" );
    
    public static final Property streetDirectory = m_model.createProperty( "http://vocab.lodum.de/istg/streetDirectory" );
    
    public static final Property streetNames = m_model.createProperty( "http://vocab.lodum.de/istg/streetNames" );
    
    public static final Property subtitle = m_model.createProperty( "http://vocab.lodum.de/istg/subtitle" );
    
    public static final Property technic = m_model.createProperty( "http://vocab.lodum.de/istg/technic" );
    
    public static final Property type = m_model.createProperty( "http://vocab.lodum.de/istg/type" );
    
    public static final Property Icon = m_model.createProperty( "http://vocab.lodum.de/istg/icon" );
    
    public static final Property urbanDistrict = m_model.createProperty( "http://vocab.lodum.de/istg/urbanDistrict" );
    
    public static final Property volume_part = m_model.createProperty( "http://vocab.lodum.de/istg/volume-part" );
    
    public static final Property yearEstimated = m_model.createProperty( "http://vocab.lodum.de/istg/yearEstimated" );
    
    public static final Resource Resource = m_model.createResource( "http://vocab.lodum.de/istg/Resource" );
    
    public static final Resource WrittenResource = m_model.createResource( "http://vocab.lodum.de/istg/WrittenResource" );
    
}
