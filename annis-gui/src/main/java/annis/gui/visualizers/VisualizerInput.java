/*
 * Copyright 2011 Corpuslinguistic working group Humboldt University Berlin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package annis.gui.visualizers;

import annis.gui.MatchedNodeColors;
import annis.gui.resultview.SingleResultPanel;
import annis.gui.resultview.VisualizerPanel;
import annis.service.ifaces.AnnisResult;
import annis.service.objects.AnnisResultImpl;
import annis.utils.LegacyGraphConverter;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sCorpusStructure.SDocument;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.STextualDS;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCore.SNode;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.jdom.Document;

/**
 * Contains all needed data for a visualizer to perform the visualization.
 *
 * @author Thomas Krause <krause@informatik.hu-berlin.de>
 */
public class VisualizerInput
{

  private SDocument document;
  private String namespace = "";
  private String paula = null;
  private Map<SNode, Long> markedAndCovered;
  private Map<String, String> markableMap = new HashMap<String, String>();
  private Map<String, String> markableExactMap = new HashMap<String, String>();
  private String id = "";
  private String contextPath;
  private String annisRemoteServiceURL;
  private String dotPath;
  private Document paulaJDOM = null;
  private AnnisResult result;
  private Properties mappings;
  private String resourcePathTemplate = "%s";
  private List<String> mediaIDs;
  private List<SNode> token;
  private Set<String> tokenAnnos;
  private STextualDS text;
  private SingleResultPanel singleResultPanel;
  private String segmentationName;
  private List<VisualizerPanel> mediaVisualizer;
  private VisualizerPanel visPanel;

  public String getAnnisRemoteServiceURL()
  {
    return annisRemoteServiceURL;
  }

  /**
   * Set the URL which is configured for the Annis installation.
   *
   * @param annisRemoteServiceURL
   */
  public void setAnnisRemoteServiceURL(String annisRemoteServiceURL)
  {
    this.annisRemoteServiceURL = annisRemoteServiceURL;
  }

  /**
   * Gets the context path of this Annis installation.
   *
   * @return The context path, beginning with an "/" but *not* ending with it.
   */
  public String getContextPath()
  {
    return contextPath;
  }

  /**
   * Sets the context path of this Annis installation.
   *
   * @param contextPath The context path, beginning with an "/" but *not* ending
   * with it.
   */
  public void setContextPath(String contextPath)
  {
    this.contextPath = contextPath;
  }

  /**
   * Get the path to the dot graph layout generator program.
   *
   * @deprecated For configuration of visualizers please use the more general
   * {@link #getMappings()} .
   */
  @Deprecated
  public String getDotPath()
  {
    return dotPath;
  }

  /**
   * Set the path to the dot graph layout generator program.
   *
   * @param dotPath
   * @deprecated For configuration of visualizers please use the more general
   * {@link #setMappings(Properties)} .
   */
  @Deprecated
  public void setDotPath(String dotPath)
  {
    this.dotPath = dotPath;
  }

  /**
   * Gets an optional result id to be used by {@link #writeOutput(Writer)}
   *
   * @return
   */
  public String getId()
  {
    return id;
  }

  /**
   * Sets an optional result id to be used by {@link #writeOutput(Writer)}
   *
   * @param id result id to be used in output
   */
  public void setId(String id)
  {
    this.id = id;
  }

  /**
   * Get the mappings for a visualizers. Mappings are visualizer specific
   * properties, that can be configured in the resolver table of the database
   *
   * @return The mappings as properties.
   */
  public Properties getMappings()
  {
    return mappings;
  }

  /**
   * Set the mappings for a visualizers. Mappings are visualizer specific
   * properties, that can be configured in the resolver table of the database
   *
   * @param mappings The new mappings.
   */
  public void setMappings(Properties mappings)
  {
    this.mappings = mappings;
  }

  /**
   * Same as {@link #getMarkableMap() } except that this only includes the
   * really matched nodes and not covered token.
   *
   * @return
   */
  @Deprecated
  public Map<String, String> getMarkableExactMap()
  {
    return markableExactMap;
  }

  @Deprecated
  public void setMarkableExactMap(Map<String, String> markableExactMap)
  {
    this.markableExactMap = markableExactMap;
  }

  /**
   * Gets the map of markables used by {@link #writeOutput(Writer)}. The key of
   * this map must be the corresponding node id of annotations or tokens. The
   * values must be HTML compatible color definitions like #000000 or red. For
   * detailed information on HTML color definition refer to
   * {@link http://www.w3schools.com/HTML/html_colornames.asp}
   *
   * @return
   */
  @Deprecated
  public Map<String, String> getMarkableMap()
  {
    return markableMap;
  }

  /**
   * Sets the map of markables used by {@link #writeOutput(Writer)}. The key of
   * this map must be the corresponding node id of annotations or tokens. The
   * values must be HTML compatible color definitions like #000000 or red. For
   * detailed information on HTML color definition refer to
   * {@link http://www.w3schools.com/HTML/html_colornames.asp}
   *
   * @param markableMap
   */
  @Deprecated
  public void setMarkableMap(Map<String, String> markableMap)
  {
    this.markableMap = markableMap;
  }

  /**
   * This map is used for calculating the colors of a matching node.
   *
   * All Nodes, which are not included, should not be colorized. For getting
   * HEX-values according to html or css standards, it is possible to use
   * {@link MatchedNodeColors}.
   *
   */
  public void setMarkedAndCovered(Map<SNode, Long> markedAndCovered)
  {
    this.markedAndCovered = markedAndCovered;
  }

  /**
   * This map is used for calculating the colors of a matching node.
   *
   * All Nodes, which are not included, should not be colorized. For getting
   * HEX-values according to html or css standards, it is possible to use
   * {@link MatchedNodeColors}.
   *
   */
  public Map<SNode, Long> getMarkedAndCovered()
  {
    return markedAndCovered;
  }

  /**
   * Gets the namespace to be processed by {@link #writeOutput(Writer)}.
   *
   * @return
   */
  public String getNamespace()
  {
    return namespace;
  }

  /**
   * Sets the namespace to be processed by {@link #writeOutput(Writer)}.
   *
   * @param namespace Namespace to be processed
   */
  public void setNamespace(String namespace)
  {
    this.namespace = namespace;
  }

  @Deprecated
  public AnnisResult getResult()
  {
    if (result == null)
    {
      result =
        new AnnisResultImpl(LegacyGraphConverter.convertToAnnotationGraph(
        document));
    }
    return result;
  }

  public SDocument getDocument()
  {
    return document;
  }

  public void setDocument(SDocument document)
  {
    this.document = document;
  }

  /**
   * Alias for {@link VisualizerInput#setDocument(de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sCorpusStructure.SDocument)
   */
  public void setResult(SDocument document)
  {
    setDocument(document);
  }

  /**
   * Alias for {@link VisualizerInput#getDocument()}
   */
  public SDocument getSResult()
  {
    return getDocument();
  }

  public String getResourcePathTemplate()
  {
    return resourcePathTemplate;
  }

  public void setResourcePathTemplate(String resourcePathTemplate)
  {
    this.resourcePathTemplate = resourcePathTemplate;
  }

  /**
   * Returns a valid URL/path for which a relative (from the class package)
   * resource can be accessed.
   *
   * @param resource
   * @return
   */
  public String getResourcePath(String resource)
  {
    return String.format(resourcePathTemplate, resource);
  }

  /**
   * mediaVisIds This sets the id of possible media visualizer. We use this to
   * resolve the javascript api in the html frontend. The List could be null,
   * which means, that there was no media visualizer triggered in the resovler
   * entries
   *
   * @param mediaVisTriggered
   */
  public void setMediaIDs(List<String> mediaIDs)
  {
    this.mediaIDs = mediaIDs;
  }

  /**
   * returns the media visualizer ids. We insert the iframe in a div block with
   * this id: "resolver-resultNum-numOfVis"
   *
   * @return
   */
  public List<String> getMediaIDs()
  {
    return mediaIDs;
  }

  /**
   * should contains a list of all token of a the which is available with
   * {@link VisualizerInput#getSResult()}.
   */
  public void setToken(List<SNode> token)
  {
    this.token = token;
  }

  /**
   * should contains a list of all token of a the which is available with
   * {@link VisualizerInput#getSResult()}.
   *
   * @return TODO at the moment it's not certain, that token are nodes of the
   * {@link VisualizerInput#getSResult()}.
   *
   */
  public List<SNode> getToken()
  {
    return this.token;
  }

  /**
   * Set all token annotations which should be displayed by the visualizer and
   * correspondands to the annos choosen by the user in the annis gui.
   */
  public void setVisibleTokenAnnos(Set<String> tokenAnnos)
  {
    this.tokenAnnos = tokenAnnos;
  }

  /**
   * This token annotation should displayed by the visualizer and is selected by
   * the user in the annis gui.
   */
  public Set<String> getVisibleTokenAnnos()
  {
    return this.tokenAnnos;
  }

  // TODO find out why we need this.
  public void setText(STextualDS text)
  {
    this.text = text;
  }

  // TODO find out why we need this
  public STextualDS getText()
  {
    return this.text;
  }

  // TODO find out why we need this
  public void setSingleResultPanelRef(SingleResultPanel parentPanel)
  {
    this.singleResultPanel = parentPanel;
  }

  // TODO find out why we need this
  public SingleResultPanel getSingleResultPanel()
  {
    return this.singleResultPanel;
  }

  public void setSegmentationName(String segmentationName)
  {
    this.segmentationName = segmentationName;
  }

  /**
   * @return the segmentationName
   */
  public String getSegmentationName()
  {
    return segmentationName;
  }

  /**
   * All available visualizer for this result.
   *
   * @param mediaVisualizer
   */
  public void setMediaVisualizer(List<VisualizerPanel> mediaVisualizer)
  {
    this.mediaVisualizer = mediaVisualizer;
  }

  public List<VisualizerPanel> getMediaVisualizer()
  {
    return mediaVisualizer;
  }

  /**
   * @return the visPanel
   */
  public VisualizerPanel getVisPanel()
  {
    return visPanel;
  }

  /**
   * @param visPanel this should be the parent VisualizerPanel for the
   * visualizer which render this result
   */
  public void setVisPanel(VisualizerPanel visPanel)
  {
    this.visPanel = visPanel;
  }
}
