/**
 * eAdventure is a research project of the
 *    e-UCM research group.
 *
 *    Copyright 2005-2014 e-UCM research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    e-UCM is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          CL Profesor Jose Garcia Santesmases 9,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.eucm.ead.editor.model;

import com.badlogic.gdx.utils.Array;
import es.eucm.ead.editor.model.events.FieldEvent;
import es.eucm.ead.editor.model.events.ListEvent;
import es.eucm.ead.editor.model.events.LoadEvent;
import es.eucm.ead.editor.model.events.MapEvent;
import es.eucm.ead.editor.model.events.ModelEvent;
import es.eucm.ead.editor.model.events.MultipleEvent;
import es.eucm.ead.editor.search.Index;
import es.eucm.ead.schema.actors.Scene;
import es.eucm.ead.schema.editor.actors.EditorScene;
import es.eucm.ead.schema.editor.game.EditorGame;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Editor model. Contains all the data of the current game project.
 */
public class Model {

	private Index index;

	private EditorGame game;

	private Map<String, EditorScene> scenes;

	private IdentityHashMap<Object, Array<ModelListener>> listeners;

	public Model() {
		index = new Index();
		scenes = new HashMap<String, EditorScene>();
		listeners = new IdentityHashMap<Object, Array<ModelListener>>();
	}

	public EditorGame getGame() {
		return game;
	}

	public void setGame(EditorGame game) {
		this.game = game;
		index.loadGame(game);
	}

	public Map<String, EditorScene> getScenes() {
		return scenes;
	}

	public void setScenes(Map<String, EditorScene> scenes) {
		this.scenes = scenes;
		for (Scene s : scenes.values()) {
			index.loadScene(s);
		}
	}

	public EditorScene getEditScene() {
		return scenes.get(game.getEditScene());
	}

	/**
	 * Search the index for a particular query text
	 * 
	 * @param queryText
	 *            to search (in all fields of all indexed objects)
	 * @return ranked results
	 */
	public Index.SearchResult search(String queryText) {
		return index.search(queryText);
	}

	/**
	 * Adds a listener to listen to loading events (essentially, listeners are
	 * notified when a new game project is loaded). Load listeners are
	 * perennial, i.e., they are not deleted when a new game is loaded
	 * 
	 * @param listener
	 *            the listener
	 */
	public void addLoadListener(ModelListener<LoadEvent> listener) {
		this.addListener(this, listener);
	}

	/**
	 * Adds a field listener. Whenever the indicated fields (indicated by
	 * {@link FieldListener#listenToField(FieldNames)}) change in target, the
	 * listener is notified.
	 * 
	 * @param target
	 *            the object whose fields must be listened
	 * @param listener
	 *            the listener
	 */
	public void addFieldListener(Object target, FieldListener listener) {
		addListener(target, listener);
	}

	/**
	 * Adds a list listener. The listener will be notified whenever the given
	 * list changes
	 * 
	 * @param list
	 *            the list to listen
	 * @param listener
	 *            the listener
	 */
	public void addListListener(List list, ModelListener<ListEvent> listener) {
		addListener(list, listener);
	}

	/**
	 * Adds a map listener. The listener will be notified whenever the given map
	 * changes
	 * 
	 * @param map
	 *            the map to listen
	 * @param listener
	 *            the listener
	 */
	public void addMapListener(Map map, ModelListener<MapEvent> listener) {
		addListener(map, listener);
	}

	private void addListener(Object target, ModelListener listener) {
		Array<ModelListener> listeners = this.listeners.get(target);
		if (listeners == null) {
			listeners = new Array<ModelListener>();
			this.listeners.put(target, listeners);
		}
		listeners.add(listener);
	}

	/**
	 * Notifies a model event to listeners. If the event is instance of
	 * {@link MultipleEvent}, each of the events that contains is individually
	 * notified.
	 * 
	 * @param event
	 *            the event to notify
	 */
	public void notify(ModelEvent event) {
		if (event instanceof MultipleEvent) {
			for (ModelEvent e : ((MultipleEvent) event).getEvents()) {
				index.notify(e);
				notify(e);
			}
		} else {
			index.notify(event);
			Array<ModelListener> listeners = this.listeners.get(event
					.getTarget());
			if (listeners != null) {
				FieldNames fieldName = event instanceof FieldEvent ? ((FieldEvent) event)
						.getField() : null;
				for (ModelListener listener : listeners) {
					if (fieldName != null && listener instanceof FieldListener) {
						if (((FieldListener) listener).listenToField(fieldName)) {
							listener.modelChanged(event);
						}
					} else {
						listener.modelChanged(event);
					}
				}
			}
		}
	}

	/**
	 * Clears all model listeners, except those listening directly to the Model
	 * object
	 */
	public void clearListeners() {
		// Keep model listeners
		Array<ModelListener> modelListeners = this.listeners.get(this);
		this.listeners.clear();
		this.listeners.put(this, modelListeners);
		scenes.clear();
		game = null;
	}

	/**
	 * Removes from the target listener the given listener
	 * 
	 * @param target
	 *            the target object that the given listener is listening to
	 * @param listener
	 *            the listener to remove
	 */
	public void removeListener(Object target, ModelListener listener) {
		Array<ModelListener> listeners = this.listeners.get(target);
		if (listeners != null) {
			listeners.removeValue(listener, true);
		}
	}

	/**
	 * The listener stops listening to oldTarget and starts listening to
	 * newTarget
	 * 
	 * @param oldTarget
	 *            the old target object (can be null)
	 * @param newTarget
	 *            the new target object
	 * @param listener
	 *            the listener
	 */
	public void retargetListener(Object oldTarget, Object newTarget,
			ModelListener listener) {
		removeListener(oldTarget, listener);
		addListener(newTarget, listener);
	}

	/**
	 * General interface to listen to the model
	 * 
	 * @param <T>
	 *            the type of the event
	 */
	public interface ModelListener<T extends ModelEvent> {

		/**
		 * Called when the model changed
		 * 
		 * @param event
		 *            the model event
		 */
		public void modelChanged(T event);
	}

	/**
	 * General interface to listen to fields
	 */
	public interface FieldListener extends ModelListener<FieldEvent> {

		/**
		 * @param fieldName
		 *            the field name (an object of enum type {@link FieldNames}
		 * @return true if this listener is interested in the fieldName
		 */
		boolean listenToField(FieldNames fieldName);

	}

}
