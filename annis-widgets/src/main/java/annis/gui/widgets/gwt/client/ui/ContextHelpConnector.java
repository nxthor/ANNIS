/*
 * Copyright 2013 SFB 632.
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
package annis.gui.widgets.gwt.client.ui;

import annis.gui.widgets.ContextHelp;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.VConsole;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.client.ui.VButton;
import com.vaadin.client.ui.VOverlay;
import com.vaadin.shared.ui.Connect;

/**
 *
 * @author Thomas Krause <thomas.krause@alumni.hu-berlin.de>
 */
@Connect(ContextHelp.class)
public class ContextHelpConnector extends AbstractExtensionConnector
{

  @Override
  protected void extend(ServerConnector target)
  {
    String iconURL = target.getConnection()
      .translateVaadinUri("theme://../runo/icons/16/help.png");
    
    final Widget widget =
      ((ComponentConnector) target).getWidget();
    
    final VOverlay helpIcon = new VOverlay();
    helpIcon.add(new HTML("<span>HELLO WORLD</span><img src=\"" + iconURL + "\" alt=\"Context help\" />"));
    
    
    widget.addDomHandler(new MouseDownHandler()
    {

      @Override
      public void onMouseDown(MouseDownEvent event)
      {
        VConsole.log("Showing help icon after load");
        helpIcon.setVisible(true);
        helpIcon.showRelativeTo(widget);
      }
      
    }, MouseDownEvent.getType());
    
  }
  
}
