///*******************************************************************************
// * Copyright (c) 2005, 2007 IBM Corporation and others.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the Eclipse Public License v1.0
// * which accompanies this distribution, and is available at
// * http://www.eclipse.org/legal/epl-v10.html
// *
//
// *******************************************************************************/
//package org.python.pydev.refactoring.typeinference.dltk.inferencer;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class DLTKTypeInferenceEngine implements ITypeInferencer {
//
//	private static final String NATURE = "nature";
//	private static final String TYPE_EVALUATORS = "org.eclipse.dltk.core.typeEvaluators";
//	private static final Map EVALUATORS_BY_NATURES = new HashMap();
//
//	static {
//		IExtensionPoint extensionPoint = Platform.getExtensionRegistry()
//				.getExtensionPoint(TYPE_EVALUATORS);
//		IExtension[] ext = extensionPoint.getExtensions();
//// ArrayList resolvers = new ArrayList();
//		for (int a = 0; a < ext.length; a++) {
//			IConfigurationElement[] elements = ext[a]
//					.getConfigurationElements();
//			IConfigurationElement myElement = elements[0];
//			try {
//				String nature = myElement.getAttribute(NATURE);
//				List list = (List) EVALUATORS_BY_NATURES.get(nature);
//				if (list == null) {
//					list = new ArrayList();
//					EVALUATORS_BY_NATURES.put(nature, list);
//				}
//// ITypeInferencer resolver = (ITypeInferencer) myElement
//// .createExecutableExtension("evaluator");
//// resolvers.add(resolver);
//// list.add(resolver);
//				list.add(myElement);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	public DLTKTypeInferenceEngine() {
//	}
//
////	public IEvaluatedType evaluateType(AbstractTypeGoal goal, int time) {
////		String nature = goal.getContext().getLangNature();
////		List list = (List) EVALUATORS_BY_NATURES.get(nature);
////		if (list != null) {
////			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
////				IConfigurationElement element = (IConfigurationElement) iterator
////						.next();
////				ITypeInferencer ti;
////				try {
////					ti = (ITypeInferencer) element
////							.createExecutableExtension("evaluator");
////				} catch (CoreException e) {
////					e.printStackTrace();
////					continue;
////				}
////// ITypeInferencer ti = (ITypeInferencer) iterator.next();
////				System.out.println();
////				IEvaluatedType type = ti.evaluateType(goal, time);
////				if (type != null && !(type instanceof UnknownType)) {
////					return type;
////				}
////			}
////		}
////		return null;
////	}
//}
