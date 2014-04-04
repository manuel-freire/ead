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
package es.eucm.ead.editor.view.widgets.engine.wrappers.transformer;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import es.eucm.ead.editor.control.Clipboard.CopyListener;
import es.eucm.ead.editor.control.Controller;
import es.eucm.ead.editor.control.actions.model.RemoveFromScene;
import es.eucm.ead.editor.control.actions.model.Reorder;
import es.eucm.ead.editor.view.widgets.AbstractWidget;
import es.eucm.ead.editor.view.widgets.engine.wrappers.transformer.listeners.MoveListener;
import es.eucm.ead.editor.view.widgets.engine.wrappers.transformer.listeners.MoveOriginListener;
import es.eucm.ead.editor.view.widgets.engine.wrappers.transformer.listeners.RotateListener;
import es.eucm.ead.editor.view.widgets.engine.wrappers.transformer.listeners.ScaleListener;
import es.eucm.ead.engine.EntitiesLoader;
import es.eucm.ead.engine.entities.ActorEntity.EntityGroup;
import es.eucm.ead.schema.entities.ModelEntity;

import java.util.List;

public class SelectedOverlay extends AbstractWidget implements CopyListener {

	private static final float ROTATE_OFFSET = 20.0f;

	private static final float HANDLE_SIZE = 12.0f;

	private Handle[] handles;

	private Controller controller;

	private EntitiesLoader entitiesLoader;

	public SelectedOverlay(EntitiesLoader entitiesLoader, Controller c,
			Skin skin) {
		this.setRequestKeyboardFocus(true);
		this.entitiesLoader = entitiesLoader;
		Drawable drawable = skin.getDrawable("white-bg");

		this.controller = c;
		handles = new Handle[10];
		for (int i = 0; i < 10; i++) {
			Color color = Color.BLACK;
			switch (i) {
			// Handle [4] is center handle
			case 4:
				color = Color.ORANGE;
				break;
			// Handle [9] is rotation handle
			case 9:
				color = Color.GREEN;
				break;
			}
			handles[i] = new Handle(drawable, color);
			handles[i].setSize(HANDLE_SIZE, HANDLE_SIZE);
			addActor(handles[i]);

			// Add listener
			switch (i) {
			case 4:
				handles[i]
						.addListener(new MoveOriginListener(controller, this));
				break;
			case 9:
				handles[i].addListener(new RotateListener(controller, this));
				break;
			default:
				handles[i].addListener(new ScaleListener(controller, this,
						i % 3 - 1, i / 3 - 1));
				break;
			}
		}
		addListener(new MoveListener(controller, this));
		addListener(new InputListener() {

			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				switch (keycode) {
				case Keys.FORWARD_DEL:
					delete();
					return true;
				case Keys.PAGE_DOWN:
					changeZ(1);
					break;
				case Keys.PAGE_UP:
					changeZ(-1);
					break;
				}
				return false;
			}
		});
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

	}

	private void delete() {
		controller.action(RemoveFromScene.class, getCurrentScene(),
				getSelectedSceneElement());
	}

	private void changeZ(int zOffset) {
		ModelEntity element = getSelectedSceneElement();
		ModelEntity scene = getCurrentScene();
		List list = scene.getChildren();
		controller.action(Reorder.class, element, zOffset, true, list);
	}

	private ModelEntity getSelectedSceneElement() {
		Actor a = getParent();
		return ((EntityGroup) a).getEntiy().getModelEntity();
	}

	private ModelEntity getCurrentScene() {
		return null;
	}

	@Override
	protected void setParent(Group parent) {
		if (getParent() instanceof EntityGroup) {
			EntityGroup oldParent = (EntityGroup) getParent();
		}
		super.setParent(parent);
		if (parent != null) {
			validate();
		}
	}

	public void validate() {
		Actor parent = getParent();
		setSize(parent.getWidth(), parent.getHeight());
		setOrigin(parent.getOriginX(), parent.getOriginY());
		layout();
	}

	public void layout() {
		float w = getWidth();
		float h = getHeight();
		float x = 0;
		float y = 0;
		float hw = HANDLE_SIZE / getParent().getScaleX();
		float hh = HANDLE_SIZE / getParent().getScaleY();
		for (int i = 0; i < handles.length; i++) {
			handles[i].setSize(hw, hh);
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				// if center
				if (i == 1 && j == 1) {
					handles[4].setPosition(getOriginX(), getOriginY());
				} else {
					switch (j) {
					case 0:
						x = 0;
						break;
					case 1:
						x = (w / 2.0f) - (hw / 2.0f);
						break;
					case 2:
						x = w - hw;
						break;
					}
					switch (i) {
					case 2:
						y = h - hh;
						break;
					case 1:
						y = (h / 2.0f) - (hh / 2.0f);
						break;
					case 0:
						y = 0;
						break;
					}
					handles[i * 3 + j].setPosition(x, y);
				}
			}
		}
		handles[9].setPosition(w / 2.0f - (hw / 2.0f), h - (hh / 2.0f)
				+ ROTATE_OFFSET);
	}

	@Override
	public Object copy(boolean cut) {
		ModelEntity sceneElement = getSelectedSceneElement();
		if (cut) {
			delete();
		}
		return sceneElement;
	}
}
