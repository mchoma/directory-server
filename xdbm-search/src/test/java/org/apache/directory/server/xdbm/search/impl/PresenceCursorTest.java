/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */
package org.apache.directory.server.xdbm.search.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.directory.server.schema.registries.*;
import org.apache.directory.server.schema.SerializableComparator;
import org.apache.directory.server.schema.bootstrap.*;
import org.apache.directory.server.xdbm.Store;
import org.apache.directory.server.xdbm.tools.StoreUtils;
import org.apache.directory.server.xdbm.tools.IndexUtils;
import org.apache.directory.server.core.partition.impl.btree.jdbm.JdbmIndex;
import org.apache.directory.server.core.partition.impl.btree.jdbm.JdbmStore;
import org.apache.directory.shared.ldap.constants.SchemaConstants;
import org.apache.directory.shared.ldap.filter.PresenceNode;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.naming.directory.Attributes;
import java.io.File;
import java.util.Set;
import java.util.HashSet;


/**
 * TODO doc me!
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $$Rev$$
 */
public class PresenceCursorTest
{
    private static final Logger LOG = LoggerFactory.getLogger( PresenceCursorTest.class.getSimpleName() );

    File wkdir;
    Store<Attributes> store;
    Registries registries = null;
    AttributeTypeRegistry attributeRegistry;


    public PresenceCursorTest() throws Exception
    {
        // setup the standard registries
        BootstrapSchemaLoader loader = new BootstrapSchemaLoader();
        OidRegistry oidRegistry = new DefaultOidRegistry();
        registries = new DefaultRegistries( "bootstrap", loader, oidRegistry );
        SerializableComparator.setRegistry( registries.getComparatorRegistry() );

        // load essential bootstrap schemas
        Set<Schema> bootstrapSchemas = new HashSet<Schema>();
        bootstrapSchemas.add( new ApachemetaSchema() );
        bootstrapSchemas.add( new ApacheSchema() );
        bootstrapSchemas.add( new CoreSchema() );
        bootstrapSchemas.add( new SystemSchema() );
        loader.loadWithDependencies( bootstrapSchemas, registries );
        attributeRegistry = registries.getAttributeTypeRegistry();
    }


    @Before
    public void createStore() throws Exception
    {
        destryStore();

        // setup the working directory for the store
        wkdir = File.createTempFile( getClass().getSimpleName(), "db" );
        wkdir.delete();
        wkdir = new File( wkdir.getParentFile(), getClass().getSimpleName() );
        wkdir.mkdirs();

        // initialize the store
        store = new JdbmStore<Attributes>();
        store.setName( "example" );
        store.setCacheSize( 10 );
        store.setWorkingDirectory( wkdir );
        store.setSyncOnWrite( false );

        store.addIndex( new JdbmIndex( SchemaConstants.OU_AT_OID ) );
        store.addIndex( new JdbmIndex( SchemaConstants.CN_AT_OID ) );
        StoreUtils.loadExampleData( store, registries );
        LOG.debug( "Created new store" );
    }


    @After
    public void destryStore() throws Exception
    {
        if ( store != null )
        {
            store.destroy();
        }

        store = null;
        if ( wkdir != null )
        {
            FileUtils.deleteDirectory( wkdir );
        }

        wkdir = null;
    }


    @Test
    public void testIndexedAttributes() throws Exception
    {
        PresenceNode node = new PresenceNode( SchemaConstants.CN_AT_OID );
        PresenceEvaluator evaluator = new PresenceEvaluator( node, store, registries );
        PresenceCursor cursor = new PresenceCursor( store, evaluator );

        cursor.beforeFirst();
        assertTrue( cursor.next() );
        assertEquals( 5, ( long ) cursor.get().getId() );
        assertTrue( cursor.next() );
        assertEquals( 6, ( long ) cursor.get().getId() );
        assertTrue( cursor.next() );
        assertEquals( 7, ( long ) cursor.get().getId() );
        assertFalse( cursor.next() );

        node = new PresenceNode( SchemaConstants.OU_AT_OID );
        evaluator = new PresenceEvaluator( node, store, registries );
        cursor = new PresenceCursor( store, evaluator );

        cursor.beforeFirst();
        assertTrue( cursor.next() );
        assertEquals( 2, ( long ) cursor.get().getId() );
        assertTrue( cursor.next() );
        assertEquals( 3, ( long ) cursor.get().getId() );
        assertTrue( cursor.next() );
        assertEquals( 4, ( long ) cursor.get().getId() );
        assertTrue( cursor.next() );
        assertEquals( 5, ( long ) cursor.get().getId() );
        assertTrue( cursor.next() );
        assertEquals( 6, ( long ) cursor.get().getId() );
        assertTrue( cursor.next() );
        assertEquals( 7, ( long ) cursor.get().getId() );
        assertFalse( cursor.next() );
    }


    @Test
    public void testNonIndexedAttributes() throws Exception
    {
        PresenceNode node = new PresenceNode( SchemaConstants.SN_AT_OID );
        PresenceEvaluator evaluator = new PresenceEvaluator( node, store, registries );
        PresenceCursor cursor = new PresenceCursor( store, evaluator );

        cursor.beforeFirst();
        assertTrue( cursor.next() );
        assertEquals( 5, ( long ) cursor.get().getId() );
        assertTrue( cursor.next() );
        assertEquals( 6, ( long ) cursor.get().getId() );
        assertTrue( cursor.next() );
        assertEquals( 7, ( long ) cursor.get().getId() );
        assertFalse( cursor.next() );

        node = new PresenceNode( SchemaConstants.O_AT_OID );
        evaluator = new PresenceEvaluator( node, store, registries );
        cursor = new PresenceCursor( store, evaluator );

        cursor.beforeFirst();
        assertTrue( cursor.next() );
        assertEquals( 1, ( long ) cursor.get().getId() );
        assertFalse( cursor.next() );
    }
}
