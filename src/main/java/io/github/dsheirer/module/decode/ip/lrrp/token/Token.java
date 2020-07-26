/*
 * *****************************************************************************
 *  Copyright (C) 2014-2020 Dennis Sheirer
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 * ****************************************************************************
 */

package io.github.dsheirer.module.decode.ip.lrrp.token;

import io.github.dsheirer.bits.CorrectedBinaryMessage;

/**
 * LRRP Token.
 *
 * LRRP packets are composed of a sequence of tokens that makeup a set of values for the LRRP report.
 */
public abstract class Token
{
    private CorrectedBinaryMessage mMessage;
    private int mOffset;

    /**
     * Constructs an instance
     * @param message containing the token and value
     * @param offset to the start of the token identifier
     */
    public Token(CorrectedBinaryMessage message, int offset)
    {
        mMessage = message;
        mOffset = offset;
    }

    /**
     * Message that contains this token and value.
     */
    protected CorrectedBinaryMessage getMessage()
    {
        return mMessage;
    }

    /**
     * Offset to the start of the token identifier within the message
     * @return bit offset value
     */
    protected int getOffset()
    {
        return mOffset;
    }

    /**
     * Type of token and value
     */
    public abstract TokenType getTokenType();

    /**
     * Number of bytes for this token including the token identifier
     */
    public int getByteLength()
    {
        return getTokenType().getLength() + 1;
    }
}