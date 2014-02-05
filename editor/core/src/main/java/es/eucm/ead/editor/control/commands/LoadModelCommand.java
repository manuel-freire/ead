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
package es.eucm.ead.editor.control.commands;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import es.eucm.ead.editor.model.Model;
import es.eucm.ead.editor.model.Project;
import es.eucm.ead.editor.model.events.LoadEvent;
import es.eucm.ead.editor.model.events.ModelEvent;
import es.eucm.ead.editor.model.events.LoadEvent.Type;
import es.eucm.ead.schema.actors.Scene;
import es.eucm.ead.schema.game.Game;

public class LoadModelCommand extends Command {

	private Game game;

	private Project project;

	private OrderedMap<String, Scene> scenes;

	public LoadModelCommand(Game game, Project project,
			OrderedMap<String, Scene> scenes) {
		this.game = game;
		this.project = project;
		this.scenes = scenes;
	}

	@Override
	public ModelEvent doCommand(Model model) {
		model.clear();
		model.setGame(game);
		for (ObjectMap.Entry<String, Scene> e : scenes.entries()) {
			model.addScene(e.key, e.value);
		}
		model.setProject(project);
		return new LoadEvent(Type.LOADED, model);
	}

	@Override
	public boolean canUndo() {
		return false;
	}

	@Override
	public ModelEvent undoCommand(Model model) {
		return null;
	}

	@Override
	public boolean combine(Command other) {
		return false;
	}
}
