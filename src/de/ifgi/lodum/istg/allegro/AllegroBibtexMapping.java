package de.ifgi.lodum.istg.allegro;


import java.awt.font.NumericShaper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bibtex.dom.BibtexEntry;
import bibtex.dom.BibtexFile;
import bibtex.dom.BibtexNode;
import bibtex.dom.BibtexString;
import bibtex.parser.BibtexParser;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import de.ifgi.lodum.vocabs.BIBO;
import de.ifgi.lodum.vocabs.ISTG;

/**
 * @author umut,johannes
 *
 */
public class AllegroBibtexMapping {



	public Model convertBibtexFile(String fileName) {
		Model model = ModelFactory.createDefaultModel();
		String baseUri = "http://data.uni-muenster.de/context/istg/allegro/";
		ArrayList<String> titles;
		ArrayList<String> authors;
		ArrayList<String> editors;
		ArrayList<String> editorsa;
		ArrayList<String> signature;
		ArrayList<String> volumes;
		ArrayList<String> numbers;
		ArrayList<String> comments;
		String[][] seriesArray;
		String[][] numbersArray;
		String resourceType;
		
		final Property foafNameMatze = model.createProperty( "http://xmlns.com/foaf/spec/#name" );
		final Property foafPersonMatze = model.createProperty( "http://xmlns.com/foaf/spec/#Person" );
		
		BibtexFile bf = new BibtexFile();
		BibtexParser bp = new BibtexParser(false);
		InputStreamReader isr;
		InputStream is;
		try {
			File bibfile = new File(fileName);
			is = new FileInputStream(bibfile);
			isr = new InputStreamReader(is);
			bp.parse(bf, isr);
			List<BibtexNode> bibNodes = bf.getEntries();

			for (BibtexNode node : bibNodes) {
				titles = new ArrayList<String>();
				authors =  new ArrayList<String>();
				editors = new ArrayList<String>();
				editorsa = new ArrayList<String>();
				signature = new ArrayList<String>();
				volumes = new ArrayList<String>();
				numbers = new ArrayList<String>();
				comments = new ArrayList<String>();
				seriesArray = new String[10][];
				numbersArray = new String[10][];
				
				resourceType = "";
				
				if (node instanceof BibtexEntry) {
					// get bibtex-entry
					BibtexEntry entry = (BibtexEntry) node;
					String type = entry.getEntryType();
					String key = entry.getEntryKey();
					key = key.split(":")[1];
					// create the new Resource
					Resource resource = model.createResource(baseUri+key);
					// add rdf:type property
					model.add(model.createStatement(resource,RDF.type,ISTG.Resource));
					model.add(model.createStatement(resource, RDF.type, ISTG.WrittenResource));
					if (type.equals("book")) {
						model.add(model.createStatement(resource, RDF.type,BIBO.Book));
						model.add(model.createStatement(resource, ISTG.Icon, BIBO.Book));
					} else if (type.equals("mvbook")) {
						model.add(model.createStatement(resource,RDF.type, BIBO.MultiVolumeBook));
						model.add(model.createStatement(resource, ISTG.Icon, BIBO.MultiVolumeBook));
					} else if (type.equals("periodical")) {
						model.add(model.createStatement(resource, RDF.type,BIBO.Periodical));
						model.add(model.createStatement(resource, ISTG.Icon, BIBO.Periodical));
					} else if (type.equals("article")) {
						model.add(model.createStatement(resource, RDF.type,BIBO.Article));
						model.add(model.createStatement(resource, ISTG.Icon, BIBO.Article));
					} else {
						model.add(model.createStatement(resource, RDF.type,BIBO.Document));
						model.add(model.createStatement(resource, ISTG.Icon, BIBO.Document));
					}
					// get all entry fields
					Map m = entry.getFields();
					// retrieve all keys, i.e. author, title etc.
					Set<?> ks = m.keySet();
					Iterator<?> ksi = ks.iterator();
					// retrieve all values
					Collection<?> c = m.values();
					Iterator<?> i = c.iterator();

					//add attributes
					while (ksi.hasNext()) {
						String s = (String) ksi.next();
						BibtexString bs = (BibtexString) i.next();
						
						if (s.contains("author")) {authors.add(bs.getContent());
						// authorlist?! immer nur 1 autor
						} else if (s.contains("title") && !s.equals("maintitlei") && !s.equals("subtitle")) { 
							//title titlei titleii, but not maintitlei or subtitle
							model.add(model.createLiteralStatement(resource,ISTG.maintitle, bs.getContent()));
						}
//						else if (s.equals("title")) {
//							model.add(model.createLiteralStatement(resource,ISTG.maintitle, bs.getContent()));
//						} 
						else if (s.contains("organization")) { //TODO
							model.add(model.createLiteralStatement(resource,DCTerms.contributor, bs.getContent()));
						} else if (s.contains("keywords")) {
							model.add(model.createLiteralStatement(resource,DC.subject, bs.getContent()));
						} else if (s.contains("location")) {
							model.add(model.createLiteralStatement(resource,ISTG.publishingLocation,bs.getContent()));
						} else if (s.contains("pages")) {
//							model.add(model.createLiteralStatement(resource,DCTerms.extent, bs.getContent()));
							model.add(model.createLiteralStatement(resource,BIBO.pages, bs.getContent()));
						} else if (s.contains("library")) {
							if (bs.getContent().contains(" * ")) {
								String[] signatures = bs.getContent().split("\\*");
								for (int j = 0; j < signatures.length; j++) {
									signature.add(signatures[j]);
								}
							} else {
								signature.add(bs.getContent());
							}
							
//							model.add(model.createLiteralStatement(resource,ISTG.signatur, bs.getContent()));
						} else if (s.contains("year")) {
							String line = bs.getContent();
							String yearString ="";
							//extract only the number, since there are often 
							Pattern p = Pattern.compile("\\d+");
							Matcher matcher = p.matcher(line); 
							while (matcher.find()) {
							   yearString+=matcher.group();
							}
							if(yearString.length()==4){
								Literal year = model.createTypedLiteral(yearString,XSDDatatype.XSDgYear);
								model.add(model.createLiteralStatement(resource,DCTerms.issued, year));
							}
						} else if (s.equals("subtitle")) {
							model.add(model.createLiteralStatement(resource,ISTG.subtitle, bs.getContent()));
						} else if (s.contains("publisher")) {
							model.add(model.createLiteralStatement(resource,DCTerms.publisher, bs.getContent().trim()));
						} else if (s.contains("number")) {
							if (!bs.getContent().equals("...")) {
								numbers.add(bs.getContent());
								if (s.equals("number")) {
									String[] temp = new String[2];
									temp[0] = "number";
									temp[1] = bs.getContent();
									numbersArray[0] = temp;
								} else if (s.equals("numberi")) {
									String[] temp = new String[2];
									temp[0] = "numberi";
									temp[1] = bs.getContent();
									numbersArray[1] = temp;
								} else if (s.equals("numberii")) {
									String[] temp = new String[2];
									temp[0] = "numberii";
									temp[1] = bs.getContent();
									numbersArray[2] = temp;
								} else if (s.equals("numberiii")) {
									String[] temp = new String[2];
									temp[0] = "numberiii";
									temp[1] = bs.getContent();
									numbersArray[3] = temp;
								} else if (s.equals("numberiv")) {
									String[] temp = new String[2];
									temp[0] = "numberiv";
									temp[1] = bs.getContent();
									numbersArray[4] = temp;
								} else if (s.equals("numberv")) {
									String[] temp = new String[2];
									temp[0] = "numberv";
									temp[1] = bs.getContent();
									numbersArray[5] = temp;
								} else if (s.equals("numbervi")) {
									String[] temp = new String[2];
									temp[0] = "numbervi";
									temp[1] = bs.getContent();
									numbersArray[6] = temp;
								} else if (s.equals("numbervi")) {
									String[] temp = new String[2];
									temp[0] = "numbervi";
									temp[1] = bs.getContent();
									numbersArray[7] = temp;
								}
							}
//							model.add(model.createLiteralStatement(resource,ISTG.reihe, bs.getContent()));
//							model.add(model.createLiteralStatement(resource,GenerateOnto.issue, bs.getContent()));
						} else if (s.contains("series")) {
							String tempSeries = "";
							if(bs.getContent().contains(" * ")){ //TODO
								String ref = bs.getContent();
								ref=ref.split(" * ")[0].replaceFirst("=","").trim();
								//Save title of the referenced title
								titles.add(bs.getContent().split("\\*")[1].trim());
								tempSeries = bs.getContent().split("\\*")[1].trim();
								model.add(model.createStatement(resource,DCTerms.isPartOf,model.createResource(baseUri+ref)));
							} else {
								tempSeries = bs.getContent();
								model.add(model.createStatement(resource,DCTerms.isPartOf,bs.getContent()));
							}
							if (s.equals("series")) {
								String[] temp = new String[2];
								temp[0] = "series";
								temp[1] = tempSeries;
								seriesArray[0] = temp;
							} else if (s.equals("seriesi")) {
								String[] temp = new String[2];
								temp[0] = "seriesi";
								temp[1] = tempSeries;
								seriesArray[1] = temp;
							} else if (s.equals("seriesii")) {
								String[] temp = new String[2];
								temp[0] = "seriesii";
								temp[1] = tempSeries;
								seriesArray[2] = temp;
							} else if (s.equals("seriesiii")) {
								String[] temp = new String[2];
								temp[0] = "seriesiii";
								temp[1] = tempSeries;
								seriesArray[3] = temp;
							} else if (s.equals("seriesiv")) {
								String[] temp = new String[2];
								temp[0] = "seriesiv";
								temp[1] = tempSeries;
								seriesArray[4] = temp;
							} else if (s.equals("seriesv")) {
								String[] temp = new String[2];
								temp[0] = "seriesv";
								temp[1] = tempSeries;
								seriesArray[5] = temp;
							} else if (s.equals("seriesvi")) {
								String[] temp = new String[2];
								temp[0] = "seriesvi";
								temp[1] = tempSeries;
								seriesArray[6] = temp;
							} else if (s.equals("seriesvii")) {
								String[] temp = new String[2];
								temp[0] = "seriesvii";
								temp[1] = tempSeries;
								seriesArray[7] = temp;
							}
						} else if (s.contains("note")) {
							comments.add(bs.getContent().trim());
//							model.add(model.createLiteralStatement(resource,RDFS.comment, bs.getContent().trim()));
						} else if (s.contains("volume")) {
							volumes.add(bs.getContent());
//							model.add(model.createLiteralStatement(resource,BIBO.volume, bs.getContent().trim()));
						} else if (s.contains("part")) {
							model.add(model.createLiteralStatement(resource,ISTG.volume_part, bs.getContent().trim()));
							/*} else if (s.equals("maintitle")) {
							model.add(model.createLiteralStatement(resource,GenerateOnto.maintitle, bs.getContent()));
						} else if (s.equals("mainsubtitle")) { 
							model.add(model.createLiteralStatement(resource,GenerateOnto.mainsubtitle, bs.getContent()));*/
						} else if (s.contains("url")) { //TODO
							model.add(model.createLiteralStatement(resource,ISTG.rezension, bs.getContent()));
						}else if (s.contains("annotation")) { 
							model.add(model.createLiteralStatement(resource,ISTG.inhaltsverzeichnis_text, bs.getContent()));
						}else if (s.contains("type")) {
							resourceType = bs.getContent();
//							model.add(model.createLiteralStatement(resource,RDFS.comment, bs.getContent()));
						}else if (s.contains("howpublished")) {
							comments.add(bs.getContent());
//							model.add(model.createLiteralStatement(resource,RDFS.comment, bs.getContent()));
						} else if (s.contains("editora")) {
							editorsa.add(bs.getContent().trim());
						} else if (s.contains("editor")&& s.contains("editora") == false) {
							editors.add(bs.getContent().trim());
						}else if(s.contains("maintitlei")){
							if(bs.getContent().contains(" * ")){ //TODO
								String ref = bs.getContent();
								ref=baseUri+ref.split(" * ")[0].replaceFirst("=","").trim();
								//Save title of the referenced title
								titles.add(bs.getContent().split("\\*")[1].trim());
								model.add(model.createStatement(resource,DCTerms.isPartOf, model.createResource(ref)));
							} else {
								model.add(model.createStatement(resource,DCTerms.isPartOf, bs.getContent()));
							}
						} else if(s.equals("category")) {
							model.add(model.createLiteralStatement(resource,ISTG.type, bs.getContent()));
						}
//						else if (s.contains("crossref")) {
//							//TODO dct:isPartOf
//							if(bs.getContent().contains("/")){
//							model.add(model.createStatement(resource,DCTerms.isPartOf, model.createResource(baseUri+bs.getContent())));
//							}
//						} else if (s.contains("bookauthor")) {
//							authors.add(bs.getContent());
//						} 
						else if (s.contains("edition")) {
							model.add(model.createLiteralStatement(resource,BIBO.edition, bs.getContent()));
						}else if (s.contains("changedate")) {
							String date = bs.getContent();
							date=date.split("-")[0].replaceFirst("-","").trim();
							String ymd = date.split("/")[0];
							ymd=ymd.substring(0, 4) +"-"+ ymd.substring(4, 6)+"-"+ymd.substring(6, 8);
							String time = date.split("/")[1];
							date = ymd +"T"+time;
							model.add(model.createLiteralStatement(resource,DCTerms.modified,model.createTypedLiteral(date, XSDDatatype.XSDdateTime)));
						}

					}
					// retrieve all values
					if (authors.size() > 0) {
						// Seq authorSequence = model.createSeq();
						String authorStringAggregate = "";
						for (int k = 0; k < authors.size(); k++) {
							if (authorStringAggregate.equals("") == false) {
								authorStringAggregate += ", ";
							}

							Resource author = model.createResource(
									baseUri
									+ key + "/author/" + k);
							// authorSequence.add(author);
//							author.addProperty(RDF.type, FOAF.Person);
//							author.addLiteral(FOAF.name, authors.get(k));
							author.addProperty(RDF.type, foafPersonMatze);
							author.addLiteral(foafNameMatze, authors.get(k));
							resource.addProperty(DC.creator, author);
							String[] splitAuthor = authors.get(k).split(",");
							if (splitAuthor.length > 1) {
								authorStringAggregate += splitAuthor[1] + " "
								+ splitAuthor[0];
							} else {
								authorStringAggregate += authors.get(k);
							}
						}
//						resource.addLiteral(ISTG.authorString,
//								authorStringAggregate.trim());
					}
					if (editors.size() > 0) {
						String editorStringAggregate = "";
						for (int k = 0; k < editors.size(); k++) {
							if (editorStringAggregate.equals("") == false) {
								editorStringAggregate += "; ";
							}

							Resource editor = model
							.createResource(baseUri
									+ key + "/editor/" + k);
//							editor.addProperty(RDF.type, FOAF.Person);
//							editor.addLiteral(FOAF.name, editors.get(k));
							editor.addProperty(RDF.type, foafPersonMatze);
							editor.addLiteral(foafNameMatze, editors.get(k));
							resource.addProperty(BIBO.editor, editor);
							editorStringAggregate += editors.get(k);
						}
//						resource.addLiteral(ISTG.editorString,
//								editorStringAggregate.trim());
					}

					if (editorsa.size() > 0) {
						// Seq editorSequence = model.createSeq();
						String editoraStringAggregate = "";
						for (int k = 0; k < editorsa.size(); k++) {
							if (editoraStringAggregate.equals("") == false) {
								editoraStringAggregate += "; ";
							}
							Resource editora = model
							.createResource(baseUri
									+ key + "/editora/" + k);
//							editora.addProperty(RDF.type, FOAF.Person);
//							editora.addLiteral(FOAF.name, editorsa.get(k));
							editora.addProperty(RDF.type, foafPersonMatze);
							editora.addLiteral(foafNameMatze, editorsa.get(k));
							// editorSequence.add(editor);
							resource.addProperty(DCTerms.contributor, editora);

							editoraStringAggregate += editorsa.get(k);

						}
//						resource.addLiteral(ISTG.editoraString,editoraStringAggregate);
					}
					
					//Build comma-seperated signature string 
					if (signature.size() > 0) {
						String signatureStringAggregate = "";
						for (int j = 0; j < signature.size(); j++) {
							if (signatureStringAggregate.equals("") == false) {
								signatureStringAggregate +=  " , ";
							}
							
							signatureStringAggregate = signatureStringAggregate + signature.get(j);
						}
						
						model.add(model.createLiteralStatement(resource,ISTG.signatur, signatureStringAggregate));
					}
					
					//Build comma-seperated volume string
					if (volumes.size() > 0) {
						String volumeStringAggregate = "";
						
						for (int j = 0; j < volumes.size(); j++) {
							if (volumeStringAggregate.equals("") == false) {
								volumeStringAggregate += " , ";
							}
							
							volumeStringAggregate = volumeStringAggregate + volumes.get(j);
						}
						
						model.add(model.createLiteralStatement(resource,BIBO.volume, volumeStringAggregate));
					}
					
					//Build comma-seperated number string
					if (numbers.size() > 0) {
						String numberStringAggregate = "";
						
						for (int j = 0; j < numbers.size(); j++) {
							if (numberStringAggregate.equals("") == false) {
								numberStringAggregate += " , ";
							}
							
							numberStringAggregate = numberStringAggregate + numbers.get(j);
						}
						
						model.add(model.createLiteralStatement(resource,ISTG.reihe, numberStringAggregate));
					}
					
					//Build comment string
					if (comments.size() > 0) {
						String commentStringAggregate = "";
						
						if (resourceType != "") {
							commentStringAggregate += resourceType;
						}
						
						for (int j = 0; j < comments.size(); j++) {
							if (commentStringAggregate.equals("") == false) {
								commentStringAggregate +="\n";
							}
							
							String[] splitComment = comments.get(j).split("¶");
							if (splitComment.length > 1) {
								String commentSubString = "";
								for (int k = 0; k < splitComment.length; k++) {
									if (commentSubString.equals("") == false) {
										commentSubString +="\n";
									}
									
									commentSubString = commentSubString + splitComment[k];
								}
								
								commentStringAggregate = commentStringAggregate + commentSubString;
								
							} else {
								commentStringAggregate = commentStringAggregate + comments.get(j);
							}
							
							//commentStringAggregate = commentStringAggregate + comments.get(j);
						}
						
						model.add(model.createLiteralStatement(resource,RDFS.comment, commentStringAggregate));
					}
					
					 
					String seriesNumbers = "";
					for (int j = 0; j < seriesArray.length; j++) {
						if (seriesArray[j] != null && numbersArray[j] != null) {
							seriesNumbers = seriesNumbers + seriesArray[j][1] + "<>" + numbersArray[j][1] +"|";
						}
					}
					if (seriesNumbers != "") {
						model.add(model.createLiteralStatement(resource, ISTG.relief, seriesNumbers));
					}
					
					//create dc:title
					 NodeIterator mtitle = model.listObjectsOfProperty(resource, ISTG.maintitle);
			
					 NodeIterator stitle = model.listObjectsOfProperty(resource, ISTG.subtitle);
					 
					 NodeIterator isPartOf = model.listObjectsOfProperty(resource, DCTerms.isPartOf);
					 
					 if(mtitle.hasNext() && stitle.hasNext()){
						 String mtitleStr = mtitle.next().asLiteral().getValue().toString().trim();
						 String stitleStr = stitle.next().asLiteral().getValue().toString().trim();
						 resource.addLiteral(DCTerms.title,mtitleStr+" : "+stitleStr);
					 }else if(mtitle.hasNext() && !stitle.hasNext()){
						 String mtitleStr = mtitle.next().asLiteral().getValue().toString().trim();
						 resource.addLiteral(DCTerms.title,mtitleStr);
					 }else if(!mtitle.hasNext() && !stitle.hasNext() && isPartOf.hasNext()){
						 //write referenced title to title if there is no title available
						 if (titles.size() > 0) {
							 resource.addLiteral(DCTerms.title,titles.get(0).toString());
						 }
						 
					 }
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return model;
	}



	public static void main(String[] args) throws FileNotFoundException {
		AllegroBibtexMapping conv = new AllegroBibtexMapping();
//		System.out.println(java.lang.Runtime.getRuntime().maxMemory()); 
		Model m=conv.convertBibtexFile("bibtex_20140916.dat"); 
		m.write(new FileOutputStream(new File("bibtex_20140916.ttl")), "TTL");
		m.write(System.out,"TTL");
	}
}
