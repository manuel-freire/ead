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

package es.eucm.ead.schema.effects;

import javax.annotation.Generated;
import com.badlogic.gdx.utils.Array;
import es.eucm.ead.schema.data.Parameter;

/**
 * Effects define events that affects/changes the game state.
 * 
 */
@Generated("org.jsonschema2pojo")
public class Effect {

	/**
	 * Expression that defines which entities this effect has to be applied to.
	 * The result of the expression must be an entity or a collection of
	 * entities.
	 * 
	 */
	private String target = "$_this";
	/**
	 * Runtime parameters. Each parameter sets a field (parameter name) with the
	 * value given by an expression (parameter value)
	 * 
	 */
	private Array<Parameter> parameters = new Array<Parameter>();

	/**
	 * Expression that defines which entities this effect has to be applied to.
	 * The result of the expression must be an entity or a collection of
	 * entities.
	 * 
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * Expression that defines which entities this effect has to be applied to.
	 * The result of the expression must be an entity or a collection of
	 * entities.
	 * 
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * Runtime parameters. Each parameter sets a field (parameter name) with the
	 * value given by an expression (parameter value)
	 * 
	 */
	public Array<Parameter> getParameters() {
		return parameters;
	}

	/**
	 * Runtime parameters. Each parameter sets a field (parameter name) with the
	 * value given by an expression (parameter value)
	 * 
	 */
	public void setParameters(Array<Parameter> parameters) {
		this.parameters = parameters;
	}

}
