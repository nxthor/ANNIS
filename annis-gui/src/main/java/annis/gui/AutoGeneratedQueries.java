/*
 * Copyright 2013 Corpuslinguistic working group Humboldt University Berlin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package annis.gui;

import com.vaadin.ui.Table;

/**
 * Wraps the auto generated queries.
 *
 * @author Benjamin Weißenfels <b.pixeldrama@gmail.com>
 */
class AutoGeneratedQueries extends Table
{

  public AutoGeneratedQueries(String caption)
  {
    super(caption);
  }

  /**
   * Sets some layout properties.
   */
  private void setUpTable()
  {
    // expand the table
    setSizeFull();

    // Allow selecting items from the table.
    setSelectable(true);

    // Send changes in selection immediately to server.
    setImmediate(true);
  }

  @Override
  public void attach()
  {
    addContainerProperty("operator", String.class, ".");
    addContainerProperty("example query", String.class,
      "tok=\"in\" .1 tok=\"Zossen\"");
    addContainerProperty("description", String.class,
      "For non-terminal nodes, precedence is determined by the right most and left most terminal children");

    // add default item
    addItem();

    setUpTable();
  }
}
