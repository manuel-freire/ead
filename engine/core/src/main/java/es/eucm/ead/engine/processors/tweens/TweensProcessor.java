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
package es.eucm.ead.engine.processors.tweens;

import ashley.core.Component;
import es.eucm.ead.engine.GameLoop;
import es.eucm.ead.engine.components.TweensComponent;
import es.eucm.ead.engine.processors.ComponentProcessor;
import es.eucm.ead.schema.components.tweens.BaseTween;
import es.eucm.ead.schema.components.tweens.Tweens;

/**
 * Converts a {@link Tweens} schema object into a {@link TweensComponent} engine
 * object
 */
public class TweensProcessor extends ComponentProcessor<Tweens> {

	public TweensProcessor(GameLoop engine) {
		super(engine);
	}

	@Override
	public Component getComponent(Tweens component) {
		TweensComponent tweens = gameLoop
				.createComponent(TweensComponent.class);
		for (BaseTween t : component.getTweens()) {
			tweens.addTween(t);
		}
		return tweens;
	}
}
