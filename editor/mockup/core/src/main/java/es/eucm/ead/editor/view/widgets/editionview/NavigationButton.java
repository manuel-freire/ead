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
package es.eucm.ead.editor.view.widgets.editionview;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import es.eucm.ead.editor.control.Controller;
import es.eucm.ead.editor.control.MockupViews;
import es.eucm.ead.editor.control.Selection;
import es.eucm.ead.editor.control.Toasts;
import es.eucm.ead.editor.control.actions.editor.ChangeMockupView;
import es.eucm.ead.editor.control.actions.model.EditScene;
import es.eucm.ead.editor.control.actions.model.scene.NewScene;
import es.eucm.ead.editor.model.Model;
import es.eucm.ead.editor.model.Model.SelectionListener;
import es.eucm.ead.editor.model.events.SelectionEvent;
import es.eucm.ead.editor.model.events.SelectionEvent.Type;
import es.eucm.ead.editor.view.builders.gallery.PlayView;
import es.eucm.ead.editor.view.builders.gallery.ScenesView;
import es.eucm.ead.editor.view.widgets.IconButton;
import es.eucm.ead.editor.view.widgets.iconwithpanel.IconWithScalePanel;
import es.eucm.ead.engine.I18N;

public class NavigationButton extends IconWithScalePanel implements
		SelectionListener {

	private static final float SEPARATION = -10, PAD = 0.4f;

	private ScenesTableList sceneList;

	private Model model;

	private ScrollPane list;

	private Toasts toasts;

	public NavigationButton(Skin skin, final Controller controller) {
		super("menu", SEPARATION, skin);
		setStyle(skin.get("white_union", IconButtonStyle.class));

		this.model = controller.getModel();

		I18N i18n = controller.getApplicationAssets().getI18N();

		InputListener changeView = new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				SceneButton button = (SceneButton) event.getListenerActor();
				controller.action(EditScene.class, controller.getModel()
						.getIdFor(button.getScene()));
			}

		};

		sceneList = new ScenesTableList(controller, changeView, "scene");

		list = new ScrollPane(sceneList, skin, "white") {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				super.draw(batch, parentAlpha);
				batch.setColor(Color.WHITE);
			}
		};
		list.setScrollingDisabled(true, false);

		IconButton goGallery = new IconButton("home", 0, skin, "white");
		float pad = goGallery.getHeight() * PAD;
		goGallery.add(i18n.m("general.scenes")).padLeft(pad).padRight(pad);
		goGallery.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				controller.action(ChangeMockupView.class, ScenesView.class);
			}
		});

		Button play = new IconButton("normalPlay", 0f, skin, "white");
		play.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				controller.action(ChangeMockupView.class, PlayView.class);
			}
		});

		panel.top();
		panel.add(play).pad(pad);
		panel.add(goGallery).pad(pad);
		panel.row();
		panel.add(list).expandX().fill().colspan(2);
		panel.row();
		float littlePad = Gdx.graphics.getHeight() * .03f;
		TextButton addButton = new TextButton(i18n.m("edition.exits.newScene"),
				skin, "white");
		addButton.pad(littlePad);
		toasts = ((MockupViews) controller.getViews()).getToasts();
		addButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				hidePanel();
				controller.action(NewScene.class, "");
				toasts.showNotification(controller.getApplicationAssets()
						.getI18N().m("edition.areNewScene"), 2f);
			}
		});
		panel.add(addButton).colspan(2);
		controller.getModel().addSelectionListener(this);
	}

	@Override
	public void showPanel() {
		sceneList.updateButtons();
		sceneList.selectScene(model.getIdFor(model.getSelection().getSingle(
				Selection.SCENE)));
		super.showPanel();
	}

	@Override
	public void modelChanged(SelectionEvent event) {
		if (event.getType() == Type.FOCUSED || event.getType() == Type.REMOVED) {
			sceneList.selectScene(model.getIdFor(model.getSelection()
					.getSingle(Selection.SCENE)));
		}
	}

	@Override
	public boolean listenToContext(String contextId) {
		return contextId.equals(Selection.SCENE);
	}
}
