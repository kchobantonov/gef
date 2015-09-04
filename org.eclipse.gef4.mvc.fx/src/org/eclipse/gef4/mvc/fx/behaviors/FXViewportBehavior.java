/*******************************************************************************
 * Copyright (c) 2014, 2015 itemis AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Alexander Nyßen (itemis AG) - initial API and implementation
 *     Matthias Wienand (itemis AG) - contribution for Bugzilla #449870 and refactorings
 *
 *******************************************************************************/
package org.eclipse.gef4.mvc.fx.behaviors;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.gef4.fx.nodes.ScrollPaneEx;
import org.eclipse.gef4.geometry.planar.AffineTransform;
import org.eclipse.gef4.mvc.behaviors.AbstractBehavior;
import org.eclipse.gef4.mvc.fx.parts.FXRootPart;
import org.eclipse.gef4.mvc.fx.viewer.FXViewer;
import org.eclipse.gef4.mvc.models.ViewportModel;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.transform.Affine;

/**
 * The {@link FXViewportBehavior} can be registered on an {@link FXRootPart} in
 * order to keep the {@link ViewportModel} in sync with the {@link ScrollPaneEx}
 * of the {@link FXViewer} and vice versa.
 *
 * @author anyssen
 * @author mwienand
 *
 */
public class FXViewportBehavior extends AbstractBehavior<Node>
		implements PropertyChangeListener {

	/*
	 * TODO: When applying the translation values stored in the ViewportModel to
	 * the ScrollPaneEx, the values will be slightly adjusted by the
	 * ScrollPaneEx. Therefore, the code that is applying the translation stored
	 * in the ScrollPaneEx back to the ViewportModel has to be secure against
	 * this adjustment. Currently, this is solved by only applying changes from
	 * the ScrollPaneEx back to the ViewportModel when #applyViewport() is not
	 * in progress.
	 */

	/**
	 * The {@link Affine} which is used to temporarily store the contents
	 * transformation.
	 */
	// TODO: make contentsTx private
	protected final Affine contentsTx = new Affine();
	private ViewportModel viewportModel;
	private final ChangeListener<Number> translateXListener = new ChangeListener<Number>() {
		@Override
		public void changed(ObservableValue<? extends Number> observable,
				Number oldValue, Number newValue) {
			if (!inApplyViewport) {
				viewportModel.setTranslateX(newValue.doubleValue());
			}
		}
	};
	private final ChangeListener<Number> translateYListener = new ChangeListener<Number>() {
		@Override
		public void changed(ObservableValue<? extends Number> observable,
				Number oldValue, Number newValue) {
			if (!inApplyViewport) {
				viewportModel.setTranslateY(newValue.doubleValue());
			}
		}
	};
	private final ChangeListener<Number> widthListener = new ChangeListener<Number>() {
		@Override
		public void changed(ObservableValue<? extends Number> observable,
				Number oldWidth, Number newWidth) {
			if (!inApplyViewport) {
				viewportModel.setWidth(newWidth.doubleValue());
			}
		}
	};
	private final ChangeListener<Number> heightListener = new ChangeListener<Number>() {
		@Override
		public void changed(ObservableValue<? extends Number> observable,
				Number oldHeight, Number newHeight) {
			if (!inApplyViewport) {
				viewportModel.setHeight(newHeight.doubleValue());
			}
		}
	};

	/**
	 * Flag which indicates if
	 * {@link #applyViewport(double, double, double, double, AffineTransform)}
	 * is currently in progress.
	 */
	boolean inApplyViewport = false;

	@Override
	public void activate() {
		super.activate();
		viewportModel = getHost().getRoot().getViewer()
				.getAdapter(ViewportModel.class);
		viewportModel.addPropertyChangeListener(this);
		getScrollPane().widthProperty().addListener(widthListener);
		getScrollPane().heightProperty().addListener(heightListener);
		getScrollPane().getScrolledPane().translateXProperty()
				.addListener(translateXListener);
		getScrollPane().getScrolledPane().translateYProperty()
				.addListener(translateYListener);
	}

	/**
	 * Applies the given translation, size, and transformation (provided by the
	 * {@link ViewportModel}) to the {@link ScrollPaneEx} of the
	 * {@link #getHost() host's} {@link FXViewer}.
	 *
	 * @param translateX
	 *            The horizontal translation.
	 * @param translateY
	 *            The vertical translation.
	 * @param width
	 *            The viewport width.
	 * @param height
	 *            The viewport height.
	 * @param contentsTransform
	 *            The contents transformation.
	 */
	protected void applyViewport(double translateX, double translateY,
			double width, double height, AffineTransform contentsTransform) {
		inApplyViewport = true;
		getScrollPane().setScrollOffsetX(translateX);
		getScrollPane().setScrollOffsetY(translateY);
		getScrollPane().setPrefWidth(width);
		getScrollPane().setPrefHeight(height);
		setTx(contentsTx, contentsTransform);
		getScrollPane().setViewportTransform(contentsTx);
		inApplyViewport = false;
	}

	@Override
	public void deactivate() {
		viewportModel.removePropertyChangeListener(this);
		getScrollPane().widthProperty().removeListener(widthListener);
		getScrollPane().heightProperty().removeListener(heightListener);
		getScrollPane().getScrolledPane().translateXProperty()
				.removeListener(translateXListener);
		getScrollPane().getScrolledPane().translateYProperty()
				.removeListener(translateYListener);
		super.deactivate();
	}

	/**
	 * Returns the {@link ScrollPaneEx} of the {@link #getHost() host's}
	 * {@link FXViewer}.
	 *
	 * @return The {@link ScrollPaneEx} of the {@link #getHost() host's}
	 *         {@link FXViewer}.
	 */
	protected ScrollPaneEx getScrollPane() {
		return ((FXViewer) getHost().getRoot().getViewer()).getScrollPane();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (ViewportModel.VIEWPORT_TRANSLATE_X_PROPERTY
				.equals(evt.getPropertyName())
				|| ViewportModel.VIEWPORT_TRANSLATE_Y_PROPERTY
						.equals(evt.getPropertyName())
				|| ViewportModel.VIEWPORT_WIDTH_PROPERTY
						.equals(evt.getPropertyName())
				|| ViewportModel.VIEWPORT_HEIGHT_PROPERTY
						.equals(evt.getPropertyName())
				|| ViewportModel.VIEWPORT_CONTENTS_TRANSFORM_PROPERTY
						.equals(evt.getPropertyName())) {
			applyViewport(viewportModel.getTranslateX(),
					viewportModel.getTranslateY(), viewportModel.getWidth(),
					viewportModel.getHeight(),
					viewportModel.getContentsTransform());
		}
	}

	/**
	 * Transfers the values of the given {@link java.awt.geom.AffineTransform}
	 * to the given {@link Affine}.
	 *
	 * @param tx
	 *            The {@link Affine} to which the transformation values are
	 *            transfered.
	 * @param at
	 *            The {@link java.awt.geom.AffineTransform} which is transfered.
	 */
	protected void setTx(Affine tx, AffineTransform at) {
		double[] m = at.getMatrix();
		tx.setMxx(m[0]);
		tx.setMxy(m[1]);
		tx.setMyx(m[2]);
		tx.setMyy(m[3]);
		tx.setTx(m[4]);
		tx.setTy(m[5]);
	}

}
