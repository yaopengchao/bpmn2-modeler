/******************************************************************************* 
 * Copyright (c) 2011, 2012 Red Hat, Inc. 
 *  All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 *
 * @author Innar Made
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.ui.wizards;

import org.eclipse.bpmn2.modeler.core.utils.ErrorUtils;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil.Bpmn2DiagramType;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.Bpmn2DiagramEditorInput;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class BPMN2DiagramCreator {

	private final static String TEMPFILE_EXTENSION = "bpmn2d"; 
//	private IFolder diagramFolder;
//	private IFile diagramFile;
//	private URI uri;

	public static Bpmn2DiagramEditorInput createDiagram(URI uri, Bpmn2DiagramType diagramType, String targetNamespace) throws CoreException {
		return createDiagram(uri, diagramType, targetNamespace, null);
	}

	public static Bpmn2DiagramEditorInput createDiagram(URI modelUri, Bpmn2DiagramType diagramType, String targetNamespace, BPMN2Editor diagramEditor) {

		String modelName = modelUri.trimFragment().trimFileExtension().lastSegment();
		final Diagram diagram = Graphiti.getPeCreateService().createDiagram("BPMN2", modelName, true);

		String diagramName = FileService.createTempName(modelName);
		URI diagramUri = URI.createFileURI(diagramName);
		TransactionalEditingDomain domain = FileService.createEmfFileForDiagram(diagramUri, diagram, diagramEditor);

		String providerId = GraphitiUi.getExtensionManager().getDiagramTypeProviderId(diagram.getDiagramTypeId());
		final Bpmn2DiagramEditorInput editorInput = new Bpmn2DiagramEditorInput(modelUri, diagramUri, providerId);
		editorInput.setInitialDiagramType(diagramType);
		editorInput.setTargetNamespace(targetNamespace);

		if (diagramEditor==null) {
			openEditor(editorInput);
		}

		return editorInput;
	}

	public static IEditorPart openEditor(final DiagramEditorInput editorInput) {
		final Object result[] = { new Object() };
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				try {
					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					IEditorPart part = null;
					part = page.findEditor(editorInput);
					if (part!=null) {
						page.activate(part);
					}
					else {
						part = page.openEditor(editorInput, BPMN2Editor.EDITOR_ID);
					}
					result[0] = part;
				} catch (PartInitException e) {
					String error = "Error while opening diagram editor";
					IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, error, e);
					ErrorUtils.showErrorWithLogging(status);
				}
			}
		});
		return (IEditorPart)result[0];
	}
}
