/*
 *   Copyright 2004 The Apache Software Foundation
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package org.apache.eve.seda ;


import java.util.EventObject ;


/**
 * Interface used to monitor Stage services.
 *
 * @author <a href="mailto:directory-dev@incubator.apache.org">
 * Apache Directory Project</a>
 * @version $Rev$
 */
public interface StageMonitor
{
    /**
     * Monitors Stage has starts.
     * 
     * @param stage the started Stage 
     */
    void started( Stage stage ) ;

    /**
     * Monitors Stage has stops.
     * 
     * @param stage the stopped Stage 
     */
    void stopped( Stage stage ) ;
    
    /**
     * Monitors StageDriver starts.
     * 
     * @param stage the Stage whose driver started
     */
    void startedDriver( Stage stage ) ;
    
    /**
     * Monitor for successful enqueue operations on the stage.
     * 
     * @param stage the stage enqueued on
     * @param event the event enqueued
     */
    void enqueueOccurred( Stage stage, EventObject event ) ;
    
    /**
     * Monitor for failed enqueue operations on the stage.
     * 
     * @param stage the stage where enqueue failed
     * @param event the event enqueue failed on
     */
    void enqueueRejected( Stage stage, EventObject event ) ;
    
    /**
     * Queue lock acquired to enqueue an event.
     * 
     * @param stage the Stage whose queue lock was acquired
     * @param event the event to be enqueued
     */
    void lockedQueue( Stage stage, EventObject event ) ;
    
    /**
     * Monitor for dequeue operations.
     * 
     * @param stage the Stage dequeued
     * @param event the event that was dequeued
     */
    void eventDequeued( Stage stage, EventObject event ) ;
    
    /**
     * Monitor for successfully completing the handling of an event.
     * 
     * @param stage the Stage processing the event 
     * @param event the event that was handled
     */
    void eventHandled( Stage stage, EventObject event ) ;
    
    // ------------------------------------------------------------------------
    // failure monitors
    // ------------------------------------------------------------------------

    /**
     * Monitors driver thread interruption failures.
     * 
     * @param stage the stage that caused the failure
     * @param fault the faulting exception
     */
    void driverFailed( Stage stage, InterruptedException fault ) ;
    
    /**
     * Monitors handler failures.
     * 
     * @param stage the stage that caused the failure
     * @param event the event the handler failed on
     * @param fault the faulting exception
     */
    void handlerFailed( Stage stage, EventObject event, Throwable fault ) ;
}
