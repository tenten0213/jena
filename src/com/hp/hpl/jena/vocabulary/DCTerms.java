/*
 * (c) Copyright 2003, Hewlett-Packard Company, all rights reserved.
 * [See end of file]
 */


/* CVS $Id: DCTerms.java,v 1.1 2003-05-08 14:42:57 andy_seaborne Exp $ */

package com.hp.hpl.jena.vocabulary ;
import com.hp.hpl.jena.rdf.model.*;
 
/**
 * Vocabulary definitions for Dublin Core Terms
 * @author Auto-generated by schemagen on 08 May 2003 09:43 
 */
public class DCTerms {
    /** <p>The RDF model that holds the vocabulary terms</p> */
    private static Model m_model = ModelFactory.createDefaultModel();
    
    /** <p>The namespace of the vocabalary as a string {@value}</p> */
    public static final String NS = "http://purl.org/dc/terms/";
    public static String getURI() { return NS ; }
    
    /** <p>The namespace of the vocabalary as a resource {@value}</p> */
    public static final Resource NAMESPACE = m_model.createResource( "http://purl.org/dc/terms/" );
    
    /** <p>A class of entity for whom the resource is intended or useful.</p> */
    public static final Property audience = m_model.createProperty( "http://purl.org/dc/terms/audience" );
    
    /** <p>Any form of the title used as a substitute or alternative to the formal title 
     *  of the resource.</p>
     */
    public static final Property alternative = m_model.createProperty( "http://purl.org/dc/terms/alternative" );
    
    /** <p>A list of subunits of the content of the resource.</p> */
    public static final Property tableOfContents = m_model.createProperty( "http://purl.org/dc/terms/tableOfContents" );
    
    /** <p>A summary of the content of the resource.</p> */
    public static final Property contentAbstract = m_model.createProperty( "http://purl.org/dc/terms/abstract" );
    
    /** <p>Date of creation of the resource.</p> */
    public static final Property created = m_model.createProperty( "http://purl.org/dc/terms/created" );
    
    /** <p>Date (often a range) of validity of a resource.</p> */
    public static final Property valid = m_model.createProperty( "http://purl.org/dc/terms/valid" );
    
    /** <p>Date (often a range) that the resource will become or did become available.</p> */
    public static final Property available = m_model.createProperty( "http://purl.org/dc/terms/available" );
    
    /** <p>Date of formal issuance (e.g., publication) of the resource.</p> */
    public static final Property issued = m_model.createProperty( "http://purl.org/dc/terms/issued" );
    
    /** <p>Date on which the resource was changed.</p> */
    public static final Property modified = m_model.createProperty( "http://purl.org/dc/terms/modified" );
    
    /** <p>The size or duration of the resource.</p> */
    public static final Property extent = m_model.createProperty( "http://purl.org/dc/terms/extent" );
    
    /** <p>The material or physical carrier of the resource.</p> */
    public static final Property medium = m_model.createProperty( "http://purl.org/dc/terms/medium" );
    
    /** <p>The described resource is a version, edition, or adaptation of the referenced 
     *  resource. Changes in version imply substantive changes in content rather than 
     *  differences in format.</p>
     */
    public static final Property isVersionOf = m_model.createProperty( "http://purl.org/dc/terms/isVersionOf" );
    
    /** <p>The described resource has a version, edition, or adaptation, namely, the 
     *  referenced resource.</p>
     */
    public static final Property hasVersion = m_model.createProperty( "http://purl.org/dc/terms/hasVersion" );
    
    /** <p>The described resource is supplanted, displaced, or superseded by the referenced 
     *  resource.</p>
     */
    public static final Property isReplacedBy = m_model.createProperty( "http://purl.org/dc/terms/isReplacedBy" );
    
    /** <p>The described resource supplants, displaces, or supersedes the referenced 
     *  resource.</p>
     */
    public static final Property replaces = m_model.createProperty( "http://purl.org/dc/terms/replaces" );
    
    /** <p>The described resource is required by the referenced resource, either physically 
     *  or logically.</p>
     */
    public static final Property isRequiredBy = m_model.createProperty( "http://purl.org/dc/terms/isRequiredBy" );
    
    /** <p>The described resource requires the referenced resource to support its function, 
     *  delivery, or coherence of content.</p>
     */
    public static final Property requires = m_model.createProperty( "http://purl.org/dc/terms/requires" );
    
    /** <p>The described resource is a physical or logical part of the referenced resource.</p> */
    public static final Property isPartOf = m_model.createProperty( "http://purl.org/dc/terms/isPartOf" );
    
    /** <p>The described resource includes the referenced resource either physically 
     *  or logically.</p>
     */
    public static final Property hasPart = m_model.createProperty( "http://purl.org/dc/terms/hasPart" );
    
    /** <p>The described resource is referenced, cited, or otherwise pointed to by the 
     *  referenced resource.</p>
     */
    public static final Property isReferencedBy = m_model.createProperty( "http://purl.org/dc/terms/isReferencedBy" );
    
    /** <p>The described resource references, cites, or otherwise points to the referenced 
     *  resource.</p>
     */
    public static final Property references = m_model.createProperty( "http://purl.org/dc/terms/references" );
    
    /** <p>The described resource is the same intellectual content of the referenced 
     *  resource, but presented in another format.</p>
     */
    public static final Property isFormatOf = m_model.createProperty( "http://purl.org/dc/terms/isFormatOf" );
    
    /** <p>The described resource pre-existed the referenced resource, which is essentially 
     *  the same intellectual content presented in another format.</p>
     */
    public static final Property hasFormat = m_model.createProperty( "http://purl.org/dc/terms/hasFormat" );
    
    /** <p>A reference to an established standard to which the resource conforms.</p> */
    public static final Property conformsTo = m_model.createProperty( "http://purl.org/dc/terms/conformsTo" );
    
    /** <p>Spatial characteristics of the intellectual content of the resoure.</p> */
    public static final Property spatial = m_model.createProperty( "http://purl.org/dc/terms/spatial" );
    
    /** <p>Temporal characteristics of the intellectual content of the resource.</p> */
    public static final Property temporal = m_model.createProperty( "http://purl.org/dc/terms/temporal" );
    
    /** <p>A class of entity that mediates access to the resource and for whom the resource 
     *  is intended or useful.</p>
     */
    public static final Property mediator = m_model.createProperty( "http://purl.org/dc/terms/mediator" );
    
    /** <p>Date of acceptance of the resource (e.g. of thesis by university department, 
     *  of article by journal, etc.).</p>
     */
    public static final Property dateAccepted = m_model.createProperty( "http://purl.org/dc/terms/dateAccepted" );
    
    /** <p>Date of a statement of copyright.</p> */
    public static final Property dateCopyrighted = m_model.createProperty( "http://purl.org/dc/terms/dateCopyrighted" );
    
    /** <p>Date of submission of the resource (e.g. thesis, articles, etc.).</p> */
    public static final Property dateSubmitted = m_model.createProperty( "http://purl.org/dc/terms/dateSubmitted" );
    
    /** <p>A general statement describing the education or training context. Alternatively, 
     *  a more specific statement of the location of the audience in terms of its 
     *  progression through an education or training context.</p>
     */
    public static final Property educationLevel = m_model.createProperty( "http://purl.org/dc/terms/educationLevel" );
    
    /** <p>Information about who can access the resource or an indication of its security 
     *  status.</p>
     */
    public static final Property accessRights = m_model.createProperty( "http://purl.org/dc/terms/accessRights" );
    
    /** <p>A bibliographic reference for the resource.</p> */
    public static final Property bibliographicCitation = m_model.createProperty( "http://purl.org/dc/terms/bibliographicCitation" );
    
    /** <p>A set of subject encoding schemes and/or formats</p> */
    public static final Resource SubjectScheme = m_model.createResource( "http://purl.org/dc/terms/SubjectScheme" );
    
    /** <p>A set of date encoding schemes and/or formats</p> */
    public static final Resource DateScheme = m_model.createResource( "http://purl.org/dc/terms/DateScheme" );
    
    /** <p>A set of format encoding schemes.</p> */
    public static final Resource FormatScheme = m_model.createResource( "http://purl.org/dc/terms/FormatScheme" );
    
    /** <p>A set of language encoding schemes and/or formats.</p> */
    public static final Resource LanguageScheme = m_model.createResource( "http://purl.org/dc/terms/LanguageScheme" );
    
    /** <p>A set of geographic place encoding schemes and/or formats</p> */
    public static final Resource SpatialScheme = m_model.createResource( "http://purl.org/dc/terms/SpatialScheme" );
    
    /** <p>A set of encoding schemes for the coverage qualifier "temporal"</p> */
    public static final Resource TemporalScheme = m_model.createResource( "http://purl.org/dc/terms/TemporalScheme" );
    
    /** <p>A set of resource type encoding schemes and/or formats</p> */
    public static final Resource TypeScheme = m_model.createResource( "http://purl.org/dc/terms/TypeScheme" );
    
    /** <p>A set of resource identifier encoding schemes and/or formats</p> */
    public static final Resource IdentifierScheme = m_model.createResource( "http://purl.org/dc/terms/IdentifierScheme" );
    
    /** <p>A set of resource relation encoding schemes and/or formats</p> */
    public static final Resource RelationScheme = m_model.createResource( "http://purl.org/dc/terms/RelationScheme" );
    
    /** <p>A set of source encoding schemes and/or formats</p> */
    public static final Resource SourceScheme = m_model.createResource( "http://purl.org/dc/terms/SourceScheme" );
    
    /** <p>Library of Congress Subject Headings</p> */
    public static final Resource LCSH = m_model.createResource( "http://purl.org/dc/terms/LCSH" );
    
    /** <p>Medical Subject Headings</p> */
    public static final Resource MESH = m_model.createResource( "http://purl.org/dc/terms/MESH" );
    
    /** <p>Dewey Decimal Classification</p> */
    public static final Resource DDC = m_model.createResource( "http://purl.org/dc/terms/DDC" );
    
    /** <p>Library of Congress Classification</p> */
    public static final Resource LCC = m_model.createResource( "http://purl.org/dc/terms/LCC" );
    
    /** <p>Universal Decimal Classification</p> */
    public static final Resource UDC = m_model.createResource( "http://purl.org/dc/terms/UDC" );
    
    /** <p>A list of types used to categorize the nature or genre of the content of the 
     *  resource.</p>
     */
    public static final Resource DCMIType = m_model.createResource( "http://purl.org/dc/terms/DCMIType" );
    
    /** <p>The Internet media type of the resource.</p> */
    public static final Resource IMT = m_model.createResource( "http://purl.org/dc/terms/IMT" );
    
    /** <p>ISO 639-2: Codes for the representation of names of languages.</p> */
    public static final Resource ISO639_2 = m_model.createResource( "http://purl.org/dc/terms/ISO639-2" );
    
    /** <p>Internet RFC 1766 'Tags for the identification of Language' specifies a two 
     *  letter code taken from ISO 639, followed optionally by a two letter country 
     *  code taken from ISO 3166.</p>
     */
    public static final Resource RFC1766 = m_model.createResource( "http://purl.org/dc/terms/RFC1766" );
    
    /** <p>A URI Uniform Resource Identifier</p> */
    public static final Resource URI = m_model.createResource( "http://purl.org/dc/terms/URI" );
    
    /** <p>The DCMI Point identifies a point in space using its geographic coordinates.</p> */
    public static final Resource Point = m_model.createResource( "http://purl.org/dc/terms/Point" );
    
    /** <p>ISO 3166 Codes for the representation of names of countries</p> */
    public static final Resource ISO3166 = m_model.createResource( "http://purl.org/dc/terms/ISO3166" );
    
    /** <p>The DCMI Box identifies a region of space using its geographic limits.</p> */
    public static final Resource Box = m_model.createResource( "http://purl.org/dc/terms/Box" );
    
    /** <p>The Getty Thesaurus of Geographic Names</p> */
    public static final Resource TGN = m_model.createResource( "http://purl.org/dc/terms/TGN" );
    
    /** <p>A specification of the limits of a time interval.</p> */
    public static final Resource Period = m_model.createResource( "http://purl.org/dc/terms/Period" );
    
    /** <p>W3C Encoding rules for dates and times - a profile based on ISO 8601</p> */
    public static final Resource W3CDTF = m_model.createResource( "http://purl.org/dc/terms/W3CDTF" );
    
    /** <p>Internet RFC 3066 'Tags for the Identification of Languages' specifies a primary 
     *  subtag which is a two-letter code taken from ISO 639 part 1 or a three-letter 
     *  code taken from ISO 639 part 2, followed optionally by a two-letter country 
     *  code taken from ISO 3166. When a language in ISO 639 has both a two-letter 
     *  and three-letter code, use the two-letter code; when it has only a three-letter 
     *  code, use the three-letter code. This RFC replaces RFC 1766.</p>
     */
    public static final Resource RFC3066 = m_model.createResource( "http://purl.org/dc/terms/RFC3066" );
    
}

/*
 *  (c) Copyright Hewlett-Packard Company 2003 
 *  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.

 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
