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

package com.eviware.soapui.impl.wsdl.submit.transports.http.support.attachments;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.httpclient.methods.RequestEntity;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.impl.wsdl.WsdlRequest;
import com.eviware.soapui.impl.wsdl.support.soap.SoapVersion;

/**
 * MimeMessage request class
 * 
 * @author ole.matzura
 */

public class WsdlRequestMimeMessageRequestEntity implements RequestEntity
{
	private final MimeMessage message;
	private final boolean isXOP;
	private final WsdlRequest wsdlRequest;

	public WsdlRequestMimeMessageRequestEntity( MimeMessage message, boolean isXOP, WsdlRequest wsdlRequest )
	{
		this.message = message;
		this.isXOP = isXOP;
		this.wsdlRequest = wsdlRequest;
	}

	public long getContentLength()
	{
		try
		{
			DummyOutputStream out = new DummyOutputStream();
			writeRequest( out );
			return out.getSize();
		}
		catch( Exception e )
		{
			SoapUI.logError( e );
			return -1;
		}
	}

	public String getContentType()
	{
		try
		{
			SoapVersion soapVersion = wsdlRequest.getOperation().getInterface().getSoapVersion();

			if( isXOP )
			{
				String header = message.getHeader( "Content-Type" )[0];

				return AttachmentUtils.buildMTOMContentType( header, wsdlRequest.getAction(), soapVersion );
			}
			else
			{
				String header = message.getHeader( "Content-Type" )[0];
				int ix = header.indexOf( "boundary" );

				return "multipart/related; type=\"" + soapVersion.getContentType() + "\"; " + "start=\""
						+ AttachmentUtils.ROOTPART_SOAPUI_ORG + "\"; " + header.substring( ix );
			}
		}
		catch( MessagingException e )
		{
			SoapUI.logError( e );
		}

		return null;
	}

	public boolean isRepeatable()
	{
		return true;
	}

	public void writeRequest( OutputStream arg0 ) throws IOException
	{
		try
		{
			arg0.write( "\r\n".getBytes() );
			( ( MimeMultipart )message.getContent() ).writeTo( arg0 );
		}
		catch( Exception e )
		{
			SoapUI.logError( e );
		}
	}

	public static class DummyOutputStream extends OutputStream
	{
		private int intLength;
		private long size = 0;

		public DummyOutputStream()
		{
			ByteArrayOutputStream tempOut = new ByteArrayOutputStream();
			tempOut.write( 1 );
			intLength = tempOut.toByteArray().length;
		}

		@Override
		public void write( int b ) throws IOException
		{
			size += intLength;
		}

		public long getSize()
		{
			return size;
		}
	}
}
