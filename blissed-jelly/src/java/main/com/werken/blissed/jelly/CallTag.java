package com.werken.blissed.jelly;

/*
 $Id: CallTag.java,v 1.2 2002-09-16 05:27:15 bob Exp $

 Copyright 2001 (C) The Werken Company. All Rights Reserved.
 
 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.
 
 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.
 
 3. The name "blissed" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Werken Company.  For written permission,
    please contact bob@werken.com.
 
 4. Products derived from this Software may not be called "blissed"
    nor may "blissed" appear in their names without prior written
    permission of The Werken Company. blissed is a registered
    trademark of The Werken Company.
 
 5. Due credit should be given to the blissed Project
    (http://blissed.werken.com/).
 
 THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.
 
 */

import com.werken.blissed.Process;
import com.werken.blissed.State;
import com.werken.blissed.Activity;
import com.werken.blissed.NoOpActivity;
import com.werken.blissed.activity.CallActivity;

import org.apache.commons.jelly.Tag;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyException;
import org.apache.commons.jelly.MissingAttributeException;

/** Call a process.
 *
 *  <p>
 *  This tag does <b>not</b> spawn a new process but simply
 *  considers another process to be the activity for the state.
 *  <p>
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
public class CallTag extends BlissedTagSupport 
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The name of the process to call. */
    private String name;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    public CallTag()
    {
        // intentionally left blank
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Set the name of the process to perform.
     *
     *  @param name The name of the process.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /** Retrieve the process.
     *
     *  @return The process.
     *
     *  @throws Exception If a process library cannot be found.
     */
    protected Process getProcess() throws Exception
    {
        return getProcess( this.name );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     org.apache.commons.jelly.Tag
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Evaluates this tag after all the tags properties
     *  have been initialized.
     *
     *  @param output The output sink.
     *
     *  @throws Exception if an error occurs.
     */
    public void doTag(XMLOutput output) throws Exception
    {
        Tag parent = getParent();

        if ( ( parent == null )
             ||
             ! ( parent instanceof StateTag ) )
        {
            throw new JellyException( "Parent is not a state" );
        }

        StateTag stateTag = (StateTag) parent;

        State state = stateTag.getState();

        if ( state.getActivity() != null
             &&
             ! ( state.getActivity() instanceof NoOpActivity ) )
        {
            throw new JellyException( "Activity already defined for state \""
                                      + state.getName()
                                      + "\"" );
        }

        if ( this.name == null )
        {
            throw new MissingAttributeException( "name" );
        }

        final String processName = this.name;

        Activity activity = new CallActivity( getProcess() );

        state.setActivity( activity );
    }
}

