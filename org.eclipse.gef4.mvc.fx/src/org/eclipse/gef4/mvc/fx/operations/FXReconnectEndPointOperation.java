/*******************************************************************************
 * Copyright (c) 2014 itemis AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Matthias Wienand (itemis AG) - initial API and implementation
 *     
 *******************************************************************************/
package org.eclipse.gef4.mvc.fx.operations;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.gef4.fx.anchors.AnchorLink;
import org.eclipse.gef4.fx.nodes.IFXConnection;

public class FXReconnectEndPointOperation extends AbstractOperation {

	public static enum AnchorKind {
		START, END
	}

	private final IFXConnection connection;
	private final AnchorLink oldAnchorLink;
	private final AnchorLink newAnchorLink;
	private final AnchorKind anchorKind;

	public FXReconnectEndPointOperation(String label, IFXConnection connection,
			AnchorLink oldAnchor, AnchorLink newAnchor, AnchorKind anchorKind) {
		super(label);
		this.connection = connection;
		this.oldAnchorLink = oldAnchor;
		this.newAnchorLink = newAnchor;
		this.anchorKind = anchorKind;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		switch (anchorKind) {
		case START:
			connection.setStartAnchorLink(newAnchorLink);
			break;
		case END:
			connection.setEndAnchorLink(newAnchorLink);
			break;
		default:
			throw new IllegalArgumentException("Unsupported AnchorKind");
		}
		return Status.OK_STATUS;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return execute(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		switch (anchorKind) {
		case START:
			connection.setStartAnchorLink(oldAnchorLink);
			break;
		case END:
			connection.setEndAnchorLink(oldAnchorLink);
			break;
		default:
			throw new IllegalArgumentException("Unsupported AnchorKind");
		}
		return Status.OK_STATUS;
	}

}
