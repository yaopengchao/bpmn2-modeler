/*******************************************************************************
 * Copyright (c) 2011, 2012 Red Hat, Inc. 
 * All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 *
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 *******************************************************************************/
package org.eclipse.bpmn2.modeler.core;

import java.util.ResourceBundle.Control;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.ITabDescriptor;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabContents;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class Bpmn2TabbedPropertySheetPage extends TabbedPropertySheetPage {

	DiagramEditor diagramEditor;
	private ISelection currentSelection;
	
	public Bpmn2TabbedPropertySheetPage(
			ITabbedPropertySheetPageContributor tabbedPropertySheetPageContributor) {
		super(tabbedPropertySheetPageContributor);
		diagramEditor = (DiagramEditor)tabbedPropertySheetPageContributor;
	}
	
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		currentSelection = selection;
		// Ignore selections from Source Viewer for now.
		// When there is better synchronization between Source Viewer and Design Editor
		// we can navigate from the selected IDOMNode to the BPMN2 model element and
		// modify the selection here...
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection ss = (IStructuredSelection)selection;
			Object elem = ss.getFirstElement();
			if (!(elem instanceof AbstractEditPart))
				return;
			super.selectionChanged(part, selection);
		}
	}
	
	public DiagramEditor getDiagramEditor() {
		return diagramEditor;
	}
}
