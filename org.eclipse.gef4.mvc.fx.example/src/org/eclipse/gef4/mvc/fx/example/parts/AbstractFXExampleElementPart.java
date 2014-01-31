package org.eclipse.gef4.mvc.fx.example.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.gef4.mvc.fx.example.model.AbstractFXGeometricElement;
import org.eclipse.gef4.mvc.fx.parts.AbstractFXContentPart;

public abstract class AbstractFXExampleElementPart extends AbstractFXContentPart implements
		PropertyChangeListener {
	
	@Override
	public AbstractFXGeometricElement<?> getContent() {
		return (AbstractFXGeometricElement<?>) super.getContent();
	}
	
	@Override
	public void activate() {
		super.activate();
		getContent().addPropertyChangeListener(this);
	}
	
	@Override
	public void deactivate() {
		getContent().removePropertyChangeListener(this);
		super.deactivate();
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == getContent()) {
			refreshVisual();
		}
	}
	
}
