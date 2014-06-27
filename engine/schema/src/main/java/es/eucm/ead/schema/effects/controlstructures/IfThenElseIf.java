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

package es.eucm.ead.schema.effects.controlstructures;

import javax.annotation.Generated;
import com.badlogic.gdx.utils.Array;
import es.eucm.ead.schema.effects.Effect;

/**
 * Typical structure if (condition) {....} else if (condition2){...} else if
 * (condition3){...}...else{...}
 * 
 */
@Generated("org.jsonschema2pojo")
public class IfThenElseIf extends If {

	/**
	 * A list of condition+effects structures that can be used to implement else
	 * if(condition2){effects2} else if (condition3){effects3}...
	 * 
	 */
	private Array<If> elseIfList = new Array<If>();
	/**
	 * A list of effects that are queued for execution if no conditions before
	 * are evaluated to false. If not present, nothing happens.
	 * 
	 */
	private Array<Effect> _else = new Array<Effect>();

	/**
	 * A list of condition+effects structures that can be used to implement else
	 * if(condition2){effects2} else if (condition3){effects3}...
	 * 
	 */
	public Array<If> getElseIfList() {
		return elseIfList;
	}

	/**
	 * A list of condition+effects structures that can be used to implement else
	 * if(condition2){effects2} else if (condition3){effects3}...
	 * 
	 */
	public void setElseIfList(Array<If> elseIfList) {
		this.elseIfList = elseIfList;
	}

	/**
	 * A list of effects that are queued for execution if no conditions before
	 * are evaluated to false. If not present, nothing happens.
	 * 
	 */
	public Array<Effect> getElse() {
		return _else;
	}

	/**
	 * A list of effects that are queued for execution if no conditions before
	 * are evaluated to false. If not present, nothing happens.
	 * 
	 */
	public void setElse(Array<Effect> _else) {
		this._else = _else;
	}

}
