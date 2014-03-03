/**
 * eAdventure is a research project of the
 *    e-UCM research group.
 *
 *    Copyright 2005-2013 e-UCM research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    e-UCM is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
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
package es.eucm.ead.editor.view.widgets.mockup.buttons;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import es.eucm.ead.editor.control.Controller;
import es.eucm.ead.editor.view.listeners.ActionOnClickListener;
import es.eucm.ead.engine.I18N;

/**
 * A widget displaying a Project, Element or Scene. (name, description,
 * image...)
 */
public class DescriptionCard extends Button {

	private static final float PREF_WIDTH = .25F;
	private static final float PREF_HEIGHT = .5F;
	private static final float TITLE_FONT_SCALE = .5f;
	private static final float DESCRIPTION_FONT_SCALE = .45f;
	private static final float DESCRIPTION_PAD_LEFT = 4f;

	private static final int MAX_TITLE_CHARACTERS = 17;
	private static final int MAX_DESCRIPTION_CHARACTERS = 92;

	private final Vector2 viewport;

	public DescriptionCard(Vector2 viewport, I18N i18n, String title,
			String description, String imageName, Skin skin) {
		super(skin);
		this.viewport = viewport;
		initialize(i18n, title, description, imageName, skin);
	}

	public DescriptionCard(Vector2 viewport, I18N i18n, String title,
			String description, String imageName, Skin skin,
			Controller controller, String actionName, Object... args) {
		super(skin);
		this.viewport = viewport;
		initialize(i18n, title, description, imageName, skin);
		addListener(new ActionOnClickListener(controller, actionName, args));
	}

	private void initialize(I18N i18n, String titl, String descrip,
			String imageName, Skin skin) {
		TextureRegion image = null;
		if (imageName == null) {
			image = skin.getRegion("icon-blitz");
		} else {
			image = skin.getRegion(imageName);
		}
		Image sceneIcon = new Image(image);
		sceneIcon.setScaling(Scaling.fit);

		if (titl == null || titl.isEmpty()) {
			titl = i18n.m("project.untitled");
		}

		if (titl.length() > MAX_TITLE_CHARACTERS) {
			titl = (titl.substring(0, MAX_TITLE_CHARACTERS) + "...");
		}
		Label title = new Label(titl, skin);
		title.setFontScale(TITLE_FONT_SCALE);
		title.setWrap(true);
		title.setAlignment(Align.center);

		if (descrip == null || descrip.isEmpty()) {
			descrip = i18n.m("project.emptydescription");
		}

		if (descrip.length() > MAX_DESCRIPTION_CHARACTERS) {
			descrip = (descrip.substring(0, MAX_DESCRIPTION_CHARACTERS) + "...");
		}
		Label description = new Label(descrip, skin);
		description.setFontScale(DESCRIPTION_FONT_SCALE);
		description.setWrap(true);
		description.setAlignment(Align.left);

		Table titleDescription = new Table();
		titleDescription.defaults().width(sceneIcon.getWidth() * 3f);
		titleDescription.add(title).expandX().fillX();
		titleDescription.row();
		titleDescription.add(description).padLeft(DESCRIPTION_PAD_LEFT)
				.expand().fill();

		add(titleDescription).expand().fill();
		add(sceneIcon).expand().fill();
	}

	@Override
	public float getPrefWidth() {
		return Math.max(super.getPrefWidth(), this.viewport == null ? 0
				: this.viewport.x * PREF_WIDTH);
	}

	@Override
	public float getPrefHeight() {
		return getPrefWidth() * PREF_HEIGHT;
	}
}