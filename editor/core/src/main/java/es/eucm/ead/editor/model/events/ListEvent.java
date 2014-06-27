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
package es.eucm.ead.editor.model.events;

import com.badlogic.gdx.utils.Array;

public class ListEvent implements ModelEvent {

	public enum Type {
		ADDED, REMOVED
	}

	private Type type;

	private Object parent;

	private Array list;

	private Object element;

	private int index;

	public ListEvent(Type type, Object parent, Array list, Object element,
			int index) {
		this.type = type;
		this.parent = parent;
		this.list = list;
		this.element = element;
		this.index = index;
	}

	public Type getType() {
		return type;
	}

	public Object getElement() {
		return element;
	}

	public int getIndex() {
		return index;
	}

	/**
	 * @return the parent of the list. Could be {@code null}
	 */
	public Object getParent() {
		return parent;
	}

	@Override
	public Array getTarget() {
		return list;
	}
}
