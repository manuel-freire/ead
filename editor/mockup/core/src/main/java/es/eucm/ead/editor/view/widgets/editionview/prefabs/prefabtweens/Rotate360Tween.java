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
package es.eucm.ead.editor.view.widgets.editionview.prefabs.prefabtweens;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import es.eucm.ead.editor.control.ComponentId;
import es.eucm.ead.editor.control.Controller;
import es.eucm.ead.schema.components.tweens.RotateTween;
import es.eucm.ead.schema.components.tweens.Tween;

public class Rotate360Tween extends PrefabTween {

	public Rotate360Tween(String icon, String name, Controller controller,
			Skin skin) {
		super(icon, name, controller, skin);
	}

	@Override
	protected Tween createTween() {
		RotateTween tween = new RotateTween();
		tween.setId(ComponentId.PREFAB_ROTATE360);
		tween.setRepeat(-1);
		tween.setDuration(2);
		tween.setRelative(true);
		tween.setRotation(360);

		return tween;
	}

}
