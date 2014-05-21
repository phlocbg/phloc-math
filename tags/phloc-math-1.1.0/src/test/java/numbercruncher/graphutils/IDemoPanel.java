/**
 * Copyright (C) 2006-2014 phloc systems
 * http://www.phloc.com
 * office[at]phloc[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package numbercruncher.graphutils;

/**
 * The demo panel interface.
 */
public interface IDemoPanel
{
  /**
   * Initialize the demo.
   */
  void initializeDemo ();

  /**
   * close the demo.
   */
  void closeDemo ();

  /**
   * Draw the contents of the panel.
   */
  void draw ();

  /**
   * Notification that the panel was resized.
   */
  void panelResized ();
}
