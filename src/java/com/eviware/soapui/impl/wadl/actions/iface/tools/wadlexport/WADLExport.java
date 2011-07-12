/*
 *  soapUI, copyright (C) 2004-2011 eviware.com 
 *
 *  soapUI is free software; you can redistribute it and/or modify it under the 
 *  terms of version 2.1 of the GNU Lesser General Public License as published by 
 *  the Free Software Foundation.
 *
 *  soapUI is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 *  See the GNU Lesser General Public License for more details at gnu.org.
 */

package com.eviware.soapui.impl.wadl.actions.iface.tools.wadlexport;

import com.eviware.soapui.impl.rest.RestService;
import com.eviware.soapui.impl.support.definition.export.WadlDefinitionExporter;
import com.eviware.soapui.impl.wsdl.actions.iface.tools.support.AbstractToolsAction;
import com.eviware.soapui.impl.wsdl.actions.iface.tools.support.ToolHost;
import com.eviware.soapui.model.iface.Interface;
import com.eviware.soapui.support.types.StringToStringMap;

/**
 * Export WADL
 * 
 * @author Oystein Gisnas <oystein@gisnas.net>
 */

public class WADLExport extends AbstractToolsAction<Interface>
{
	private String outputFolder;
	
	public WADLExport()
	{
		super( "WADL Export", "Export WADL file for REST interface" );
	}

	protected StringToStringMap initValues( Interface modelItem, Object param )
	{
		StringToStringMap values = super.initValues( modelItem, param );
		outputFolder = (String) param;

		return values;
	}

	protected void generate( StringToStringMap values, ToolHost toolHost, Interface modelItem ) throws Exception
	{
		WadlDefinitionExporter exporter = new WadlDefinitionExporter((RestService) modelItem);
		exporter.export(outputFolder);
		System.out.println("OutputFolder="+outputFolder);
	}

}
