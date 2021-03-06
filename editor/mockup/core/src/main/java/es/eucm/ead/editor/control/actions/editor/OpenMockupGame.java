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
package es.eucm.ead.editor.control.actions.editor;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.reflect.ClassReflection;

import es.eucm.ead.editor.assets.EditorGameAssets;
import es.eucm.ead.editor.control.MockupController;
import es.eucm.ead.editor.control.actions.EditorActionException;
import es.eucm.ead.editor.control.actions.editor.asynk.CloseMockupGame;
import es.eucm.ead.editor.control.actions.model.EditScene;
import es.eucm.ead.editor.control.background.BackgroundExecutor;
import es.eucm.ead.editor.control.background.BackgroundExecutor.BackgroundTaskListener;
import es.eucm.ead.editor.control.background.BackgroundTask;
import es.eucm.ead.editor.model.Model;
import es.eucm.ead.editor.model.Q;
import es.eucm.ead.editor.view.builders.ViewBuilder;
import es.eucm.ead.editor.view.builders.gallery.ScenesView;
import es.eucm.ead.schema.editor.components.EditState;
import es.eucm.ead.schema.entities.ModelEntity;

/**
 * @see OpenGame
 * 
 */
public class OpenMockupGame extends OpenGame {
	/**
	 * Saving interval in seconds.
	 */
	private static final float SAVE_DELAY = 30f;
	protected static final float ERROR_TIMEOUT = 3F;

	@Override
	public void perform(Object... args) {
		fileChosen(args[0].toString());
	}

	@Override
	protected boolean load(String gamePath) {
		if (!gamePath.endsWith("/")) {
			gamePath += "/";
		}
		if (super.load(gamePath)) {
			controller.getPreferences().flush();
			return true;
		}
		handleError();
		return false;
	}

	@Override
	protected void finishLoading(String path) {
		try {
			ViewBuilder currentView = controller.getViews().getCurrentView();
			EditorGameAssets editorGameAssets = controller
					.getEditorGameAssets();
			path = editorGameAssets.toCanonicalPath(path);
			if (!path.endsWith("/")) {
				path += "/";
			}
			super.finishLoading(path);
			if (!path.equals(editorGameAssets.getLoadingPath())) {
				throw new EditorActionException("Failed opening: " + path
						+ ", probably deleted.");
			}

			if (controller.getViews().getCurrentView() == currentView) {
				controller.action(ChangeMockupView.class, ScenesView.class);
			}
			Group rootComponent = ((MockupController) controller)
					.getRootComponent();
			rootComponent.clearActions();
			rootComponent.addAction(forever(delay(SAVE_DELAY, run(saveGame))));
		} catch (Exception eae) {
			// the project is probably corrupt; complain but continue
			Gdx.app.log("OpenLastProject", "Error opening '" + path
					+ "'; Request ignored", eae);
			handleError();
		}
	}

	private void handleError() {
		Gdx.app.postRunnable(closeGame);
	}

	private Runnable closeGame = new Runnable() {
		@Override
		public void run() {
			controller
					.action(CloseMockupGame.class,
							controller.getApplicationAssets().getI18N()
									.m("project.errorOpening"));
		}
	};

	private final Runnable saveGame = new Runnable() {
		@Override
		public void run() {
			controller.getBackgroundExecutor().submit(saveTask, saveListener);
		}

		private final BackgroundTaskListener<Boolean> saveListener = new BackgroundTaskListener<Boolean>() {

			@Override
			public void completionPercentage(float percentage) {
			}

			@Override
			public void done(BackgroundExecutor backgroundExecutor,
					Boolean result) {
			}

			@Override
			public void error(Throwable e) {
			}
		};

		private final BackgroundTask<Boolean> saveTask = new BackgroundTask<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				controller.action(ForceSave.class);
				return true;
			}
		};
	};

	protected void setEditionState(Model model) {
		ModelEntity game = model.getGame();
		EditState editState = Q.getComponent(game, EditState.class);
		if (editState.getView() != null) {
			try {
				String editScene = editState.getEditScene();
				if (editScene != null && !editScene.isEmpty()) {
					controller.action(EditScene.class, editScene);
				}

				Class viewClass = ClassReflection.forName(editState.getView());

				int i = 0;
				Object[] args = new Object[editState.getArguments().size + 1];
				args[i++] = viewClass;
				for (Object arg : editState.getArguments()) {
					args[i++] = arg;
				}
				controller.action(ChangeMockupView.class, args);
			} catch (Exception e) {
				Gdx.app.error("OpenGame",
						"Impossible to set view " + editState.getView(), e);
				handleError();
			}
		}
	}

}
