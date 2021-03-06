/*******************************************************************************
 * sdr-trunk
 * Copyright (C) 2014-2018 Dennis Sheirer
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by  the Free Software Foundation, either version 3 of the License, or  (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful,  but WITHOUT ANY WARRANTY; without even the implied
 * warranty of  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License  along with this program.
 * If not, see <http://www.gnu.org/licenses/>
 *
 ******************************************************************************/
package io.github.dsheirer.edac;

import io.github.dsheirer.bits.BinaryMessage;
import io.github.dsheirer.bits.CorrectedBinaryMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * P25 CRC check/correction methods
 */
public class CRCP25
{
    private final static Logger mLog = LoggerFactory.getLogger(CRCP25.class);

    /**
     * CRC-CCITT 16-bit checksums for a message length of 80 bits plus 16
     * additional checksums representing CRC checksum bit errors
     *
     * Generated by:
     * CRCUtil.generate( 80, 16, 0x11021l, 0xFFFFl, true );
     */
    public static final int[] CCITT_80_CHECKSUMS = new int[]
        {
            0x1BCB, 0x8DE5, 0xC6F2, 0x6B69, 0xB5B4, 0x52CA, 0x2175, 0x90BA, 0x404D,
            0xA026, 0x5803, 0xAC01, 0xD600, 0x6310, 0x3998, 0x14DC, 0x27E, 0x92F,
            0x8497, 0xC24B, 0xE125, 0xF092, 0x7059, 0xB82C, 0x5406, 0x2213, 0x9109,
            0xC884, 0x6C52, 0x3E39, 0x9F1C, 0x479E, 0x2BDF, 0x95EF, 0xCAF7, 0xE57B,
            0xF2BD, 0xF95E, 0x74BF, 0xBA5F, 0xDD2F, 0xEE97, 0xF74B, 0xFBA5, 0xFDD2,
            0x76F9, 0xBB7C, 0x55AE, 0x22C7, 0x9163, 0xC8B1, 0xE458, 0x7A3C, 0x350E,
            0x1297, 0x894B, 0xC4A5, 0xE252, 0x7939, 0xBC9C, 0x565E, 0x233F, 0x919F,
            0xC8CF, 0xE467, 0xF233, 0xF919, 0xFC8C, 0x7656, 0x333B, 0x999D, 0xCCCE,
            0x6E77, 0xB73B, 0xDB9D, 0xEDCE, 0x7EF7, 0xBF7B, 0xDFBD, 0xEFDE, 0x0001,
            0x0002, 0x0004, 0x0008, 0x0010, 0x0020, 0x0040, 0x0080, 0x0100, 0x0200,
            0x0400, 0x0800, 0x1000, 0x2000, 0x4000, 0x8000
        };

    /**
     * Confirmed Packet Data Unit CRC-9 checksums, generated by:
     *
     * long[] table = CRCUtil.generate( 135, 9, 0x259l, 0x1FF, false, Parity.NONE );
     */
    public static final int[] CRC9_CHECKSUMS = new int[]
        {
            0x1E7, 0x1F3, 0x1F9, 0x1FC, 0x0D2, 0x045, 0x122, 0x0BD, 0x15E, 0x083,
            0x141, 0x1A0, 0x0FC, 0x052, 0x005, 0x102, 0x0AD, 0x156, 0x087, 0x143,
            0x1A1, 0x1D0, 0x0C4, 0x04E, 0x00B, 0x105, 0x182, 0x0ED, 0x176, 0x097,
            0x14B, 0x1A5, 0x1D2, 0x0C5, 0x162, 0x09D, 0x14E, 0x08B, 0x145, 0x1A2,
            0x0FD, 0x17E, 0x093, 0x149, 0x1A4, 0x0FE, 0x053, 0x129, 0x194, 0x0E6,
            0x05F, 0x12F, 0x197, 0x1CB, 0x1E5, 0x1F2, 0x0D5, 0x16A, 0x099, 0x14C,
            0x08A, 0x069, 0x134, 0x0B6, 0x077, 0x13B, 0x19D, 0x1CE, 0x0CB, 0x165,
            0x1B2, 0x0F5, 0x17A, 0x091, 0x148, 0x088, 0x068, 0x018, 0x020, 0x03C,
            0x032, 0x035, 0x11A, 0x0A1, 0x150, 0x084, 0x06E, 0x01B, 0x10D, 0x186,
            0x0EF, 0x177, 0x1BB, 0x1DD, 0x1EE, 0x0DB, 0x16D, 0x1B6, 0x0F7, 0x17B,
            0x1BD, 0x1DE, 0x0C3, 0x161, 0x1B0, 0x0F4, 0x056, 0x007, 0x103, 0x181,
            0x1C0, 0x0CC, 0x04A, 0x009, 0x104, 0x0AE, 0x07B, 0x13D, 0x19E, 0x0E3,
            0x171, 0x1B8, 0x0F0, 0x054, 0x006, 0x02F, 0x117, 0x18B, 0x1C5, 0x1E2,
            0x0DD, 0x16E, 0x09B, 0x14D, 0x1A6
        };

    /**
     * CRC-32 checksums for PDU1 ( HEADER + 1 Block ) messages, generated by:
     *
     * CRCUtil.generate( 64, 32, 0x104C11DB7l, 0xFFFFFFFFl, true );
     */
    public static final long[] PDU1_CHECKSUMS = new long[]
        {
            0x86FFAACCl, 0x411F5BBDl, 0xA08FADDEl, 0x52275834l, 0x2B7322C1l,
            0x95B99160l, 0x48BC466Bl, 0xA45E2335l, 0xD22F119Al, 0x6B770616l,
            0x37DB0DD0l, 0x198D0833l, 0x8CC68419l, 0xC663420Cl, 0x61512FDDl,
            0xB0A897EEl, 0x5A34C52Cl, 0x2F7AEC4Dl, 0x97BD7626l, 0x49BE35C8l,
            0x26BF943Fl, 0x935FCA1Fl, 0xC9AFE50Fl, 0xE4D7F287l, 0xF26BF943l,
            0xF935FCA1l, 0xFC9AFE50l, 0x7C2DF1F3l, 0xBE16F8F9l, 0xDF0B7C7Cl,
            0x6DE530E5l, 0xB6F29872l, 0x5919C2E2l, 0x2EEC6FAAl, 0x1516B90El,
            0x08EBD25Cl, 0x061567F5l, 0x830AB3FAl, 0x43E5D726l, 0x23926548l,
            0x13A9BC7Fl, 0x89D4DE3Fl, 0xC4EA6F1Fl, 0xE275378Fl, 0xF13A9BC7l,
            0xF89D4DE3l, 0xFC4EA6F1l, 0xFE275378l, 0x7D732767l, 0xBEB993B3l,
            0xDF5CC9D9l, 0xEFAE64ECl, 0x75B7BCADl, 0xBADBDE56l, 0x5F0D61F0l,
            0x2DE63E23l, 0x96F31F11l, 0xCB798F88l, 0x67DC491Fl, 0xB3EE248Fl,
            0xD9F71247l, 0xECFB8923l, 0xF67DC491l, 0xFB3EE248l, 0x00000001l,
            0x00000002l, 0x00000004l, 0x00000008l, 0x00000010l, 0x00000020l,
            0x00000040l, 0x00000080l, 0x00000100l, 0x00000200l, 0x00000400l,
            0x00000800l, 0x00001000l, 0x00002000l, 0x00004000l, 0x00008000l,
            0x00010000l, 0x00020000l, 0x00040000l, 0x00080000l, 0x00100000l,
            0x00200000l, 0x00400000l, 0x00800000l, 0x01000000l, 0x02000000l,
            0x04000000l, 0x08000000l, 0x10000000l, 0x20000000l, 0x40000000l,
            0x80000000l
        };

    /**
     * CRC-32 checksums for PDU2 ( HEADER + 2 Blocks ) messages, generated by:
     *
     * CRCUtil.generate( 160, 32, 0x104C11DB7l, 0xFFFFFFFFl, true );
     */
    public static final long[] PDU2_CHECKSUMS = new long[]
        {
            0x9D231959l, 0xCE918CACl, 0x6528488Dl, 0xB2942446l, 0x5B2A9CF8l,
            0x2FF5C0A7l, 0x97FAE053l, 0xCBFD7029l, 0xE5FEB814l, 0x709FD2D1l,
            0xB84FE968l, 0x5E477A6Fl, 0xAF23BD37l, 0xD791DE9Bl, 0xEBC8EF4Dl,
            0xF5E477A6l, 0x7892B508l, 0x3E29D45Fl, 0x9F14EA2Fl, 0xCF8A7517l,
            0xE7C53A8Bl, 0xF3E29D45l, 0xF9F14EA2l, 0x7E98298Al, 0x3D2C9A1El,
            0x1CF6C3D4l, 0x0C1BEF31l, 0x860DF798l, 0x41667517l, 0xA0B33A8Bl,
            0xD0599D45l, 0xE82CCEA2l, 0x7676E98Al, 0x395BFA1El, 0x1ECD73D4l,
            0x0D063731l, 0x86831B98l, 0x41210317l, 0xA090818Bl, 0xD04840C5l,
            0xE8242062l, 0x76729EEAl, 0x3959C1AEl, 0x1ECC6E0Cl, 0x0D06B9DDl,
            0x86835CEEl, 0x412120ACl, 0x22F01E8Dl, 0x91780F46l, 0x4ADC8978l,
            0x270ECA67l, 0x93876533l, 0xC9C3B299l, 0xE4E1D94Cl, 0x7010627Dl,
            0xB808313El, 0x5E649644l, 0x2D52C5F9l, 0x96A962FCl, 0x49343FA5l,
            0xA49A1FD2l, 0x502D8132l, 0x2A764E42l, 0x175BA9FAl, 0x09CD5A26l,
            0x068623C8l, 0x01239F3Fl, 0x8091CF9Fl, 0xC048E7CFl, 0xE02473E7l,
            0xF01239F3l, 0xF8091CF9l, 0xFC048E7Cl, 0x7C62C9E5l, 0xBE3164F2l,
            0x5D783CA2l, 0x2CDC908Al, 0x140EC69El, 0x0867ED94l, 0x06537811l,
            0x8329BC08l, 0x43F450DFl, 0xA1FA286Fl, 0xD0FD1437l, 0xE87E8A1Bl,
            0xF43F450Dl, 0xFA1FA286l, 0x7F6F5F98l, 0x3DD72117l, 0x9EEB908Bl,
            0xCF75C845l, 0xE7BAE422l, 0x71BDFCCAl, 0x3ABE70BEl, 0x1F3FB684l,
            0x0DFF5599l, 0x86FFAACCl, 0x411F5BBDl, 0xA08FADDEl, 0x52275834l,
            0x2B7322C1l, 0x95B99160l, 0x48BC466Bl, 0xA45E2335l, 0xD22F119Al,
            0x6B770616l, 0x37DB0DD0l, 0x198D0833l, 0x8CC68419l, 0xC663420Cl,
            0x61512FDDl, 0xB0A897EEl, 0x5A34C52Cl, 0x2F7AEC4Dl, 0x97BD7626l,
            0x49BE35C8l, 0x26BF943Fl, 0x935FCA1Fl, 0xC9AFE50Fl, 0xE4D7F287l,
            0xF26BF943l, 0xF935FCA1l, 0xFC9AFE50l, 0x7C2DF1F3l, 0xBE16F8F9l,
            0xDF0B7C7Cl, 0x6DE530E5l, 0xB6F29872l, 0x5919C2E2l, 0x2EEC6FAAl,
            0x1516B90El, 0x08EBD25Cl, 0x061567F5l, 0x830AB3FAl, 0x43E5D726l,
            0x23926548l, 0x13A9BC7Fl, 0x89D4DE3Fl, 0xC4EA6F1Fl, 0xE275378Fl,
            0xF13A9BC7l, 0xF89D4DE3l, 0xFC4EA6F1l, 0xFE275378l, 0x7D732767l,
            0xBEB993B3l, 0xDF5CC9D9l, 0xEFAE64ECl, 0x75B7BCADl, 0xBADBDE56l,
            0x5F0D61F0l, 0x2DE63E23l, 0x96F31F11l, 0xCB798F88l, 0x67DC491Fl,
            0xB3EE248Fl, 0xD9F71247l, 0xECFB8923l, 0xF67DC491l, 0xFB3EE248l,
            0x00000001l, 0x00000002l, 0x00000004l, 0x00000008l, 0x00000010l,
            0x00000020l, 0x00000040l, 0x00000080l, 0x00000100l, 0x00000200l,
            0x00000400l, 0x00000800l, 0x00001000l, 0x00002000l, 0x00004000l,
            0x00008000l, 0x00010000l, 0x00020000l, 0x00040000l, 0x00080000l,
            0x00100000l, 0x00200000l, 0x00400000l, 0x00800000l, 0x01000000l,
            0x02000000l, 0x04000000l, 0x08000000l, 0x10000000l, 0x20000000l,
            0x40000000l, 0x80000000l
        };

    /**
     * CRC-32 checksums for PDU3 ( HEADER + 3 Blocks ) messages, generated by:
     *
     * CRCUtil.generate( 256, 32, 0x104C11DB7l, 0xFFFFFFFFl, true );
     */
    public static final long[] PDU3_CHECKSUMS = new long[]
        {
            0xAA5FA470l, 0x574F5CE3l, 0xABA7AE71l, 0xD5D3D738l, 0x68896547l,
            0xB444B2A3l, 0xDA225951l, 0xED112CA8l, 0x74E8188Fl, 0xBA740C47l,
            0xDD3A0623l, 0xEE9D0311l, 0xF74E8188l, 0x79C7CE1Fl, 0xBCE3E70Fl,
            0xDE71F387l, 0xEF38F9C3l, 0xF79C7CE1l, 0xFBCE3E70l, 0x7F8791E3l,
            0xBFC3C8F1l, 0xDFE1E478l, 0x6D907CE7l, 0xB6C83E73l, 0xDB641F39l,
            0xEDB20F9Cl, 0x74B98915l, 0xBA5CC48Al, 0x5F4EEC9El, 0x2DC7F894l,
            0x14837291l, 0x8A41B948l, 0x4740527Fl, 0xA3A0293Fl, 0xD1D0149Fl,
            0xE8E80A4Fl, 0xF4740527l, 0xFA3A0293l, 0xFD1D0149l, 0xFE8E80A4l,
            0x7D27CE89l, 0xBE93E744l, 0x5D297D79l, 0xAE94BEBCl, 0x552AD185l,
            0xAA9568C2l, 0x572A3ABAl, 0x29F59386l, 0x169A4718l, 0x092DAD57l,
            0x8496D6ABl, 0xC24B6B55l, 0xE125B5AAl, 0x72F2540El, 0x3B19A4DCl,
            0x1FEC5CB5l, 0x8FF62E5Al, 0x459B99F6l, 0x20AD4220l, 0x12362FCBl,
            0x891B17E5l, 0xC48D8BF2l, 0x60264B22l, 0x3273AB4Al, 0x1B595B7El,
            0x0FCC2364l, 0x05869F69l, 0x82C34FB4l, 0x43012901l, 0xA1809480l,
            0x52A0C49Bl, 0xA950624Dl, 0xD4A83126l, 0x68349648l, 0x367AC5FFl,
            0x9B3D62FFl, 0xCD9EB17Fl, 0xE6CF58BFl, 0xF367AC5Fl, 0xF9B3D62Fl,
            0xFCD9EB17l, 0xFE6CF58Bl, 0xFF367AC5l, 0xFF9B3D62l, 0x7DAD106Al,
            0x3CB606EEl, 0x1C3B8DACl, 0x0C7D480Dl, 0x863EA406l, 0x417FDCD8l,
            0x22DF60B7l, 0x916FB05Bl, 0xC8B7D82Dl, 0xE45BEC16l, 0x704D78D0l,
            0x3A4632B3l, 0x9D231959l, 0xCE918CACl, 0x6528488Dl, 0xB2942446l,
            0x5B2A9CF8l, 0x2FF5C0A7l, 0x97FAE053l, 0xCBFD7029l, 0xE5FEB814l,
            0x709FD2D1l, 0xB84FE968l, 0x5E477A6Fl, 0xAF23BD37l, 0xD791DE9Bl,
            0xEBC8EF4Dl, 0xF5E477A6l, 0x7892B508l, 0x3E29D45Fl, 0x9F14EA2Fl,
            0xCF8A7517l, 0xE7C53A8Bl, 0xF3E29D45l, 0xF9F14EA2l, 0x7E98298Al,
            0x3D2C9A1El, 0x1CF6C3D4l, 0x0C1BEF31l, 0x860DF798l, 0x41667517l,
            0xA0B33A8Bl, 0xD0599D45l, 0xE82CCEA2l, 0x7676E98Al, 0x395BFA1El,
            0x1ECD73D4l, 0x0D063731l, 0x86831B98l, 0x41210317l, 0xA090818Bl,
            0xD04840C5l, 0xE8242062l, 0x76729EEAl, 0x3959C1AEl, 0x1ECC6E0Cl,
            0x0D06B9DDl, 0x86835CEEl, 0x412120ACl, 0x22F01E8Dl, 0x91780F46l,
            0x4ADC8978l, 0x270ECA67l, 0x93876533l, 0xC9C3B299l, 0xE4E1D94Cl,
            0x7010627Dl, 0xB808313El, 0x5E649644l, 0x2D52C5F9l, 0x96A962FCl,
            0x49343FA5l, 0xA49A1FD2l, 0x502D8132l, 0x2A764E42l, 0x175BA9FAl,
            0x09CD5A26l, 0x068623C8l, 0x01239F3Fl, 0x8091CF9Fl, 0xC048E7CFl,
            0xE02473E7l, 0xF01239F3l, 0xF8091CF9l, 0xFC048E7Cl, 0x7C62C9E5l,
            0xBE3164F2l, 0x5D783CA2l, 0x2CDC908Al, 0x140EC69El, 0x0867ED94l,
            0x06537811l, 0x8329BC08l, 0x43F450DFl, 0xA1FA286Fl, 0xD0FD1437l,
            0xE87E8A1Bl, 0xF43F450Dl, 0xFA1FA286l, 0x7F6F5F98l, 0x3DD72117l,
            0x9EEB908Bl, 0xCF75C845l, 0xE7BAE422l, 0x71BDFCCAl, 0x3ABE70BEl,
            0x1F3FB684l, 0x0DFF5599l, 0x86FFAACCl, 0x411F5BBDl, 0xA08FADDEl,
            0x52275834l, 0x2B7322C1l, 0x95B99160l, 0x48BC466Bl, 0xA45E2335l,
            0xD22F119Al, 0x6B770616l, 0x37DB0DD0l, 0x198D0833l, 0x8CC68419l,
            0xC663420Cl, 0x61512FDDl, 0xB0A897EEl, 0x5A34C52Cl, 0x2F7AEC4Dl,
            0x97BD7626l, 0x49BE35C8l, 0x26BF943Fl, 0x935FCA1Fl, 0xC9AFE50Fl,
            0xE4D7F287l, 0xF26BF943l, 0xF935FCA1l, 0xFC9AFE50l, 0x7C2DF1F3l,
            0xBE16F8F9l, 0xDF0B7C7Cl, 0x6DE530E5l, 0xB6F29872l, 0x5919C2E2l,
            0x2EEC6FAAl, 0x1516B90El, 0x08EBD25Cl, 0x061567F5l, 0x830AB3FAl,
            0x43E5D726l, 0x23926548l, 0x13A9BC7Fl, 0x89D4DE3Fl, 0xC4EA6F1Fl,
            0xE275378Fl, 0xF13A9BC7l, 0xF89D4DE3l, 0xFC4EA6F1l, 0xFE275378l,
            0x7D732767l, 0xBEB993B3l, 0xDF5CC9D9l, 0xEFAE64ECl, 0x75B7BCADl,
            0xBADBDE56l, 0x5F0D61F0l, 0x2DE63E23l, 0x96F31F11l, 0xCB798F88l,
            0x67DC491Fl, 0xB3EE248Fl, 0xD9F71247l, 0xECFB8923l, 0xF67DC491l,
            0xFB3EE248l, 0x00000001l, 0x00000002l, 0x00000004l, 0x00000008l,
            0x00000010l, 0x00000020l, 0x00000040l, 0x00000080l, 0x00000100l,
            0x00000200l, 0x00000400l, 0x00000800l, 0x00001000l, 0x00002000l,
            0x00004000l, 0x00008000l, 0x00010000l, 0x00020000l, 0x00040000l,
            0x00080000l, 0x00100000l, 0x00200000l, 0x00400000l, 0x00800000l,
            0x01000000l, 0x02000000l, 0x04000000l, 0x08000000l, 0x10000000l,
            0x20000000l, 0x40000000l, 0x80000000l
        };

    /**
     * Performs error detection and single-bit error correction against the
     * data blocks of a PDU1 message.
     */
    public static BinaryMessage correctPDU1(BinaryMessage message)
    {
        return correctPDU(message, PDU1_CHECKSUMS, 224);
    }

    /**
     * Performs error detection and single-bit error correction against the
     * data blocks of a PDU2 message.
     */
    public static BinaryMessage correctPDU2(BinaryMessage message)
    {
        return correctPDU(message, PDU2_CHECKSUMS, 320);
    }

    /**
     * Performs error detection and single-bit error correction against the
     * data blocks of a PDU3 message.
     */
    public static BinaryMessage correctPDU3(BinaryMessage message)
    {
        return correctPDU(message, PDU3_CHECKSUMS, 416);
    }

    public static BinaryMessage correctPDU(BinaryMessage message, long[] checksums, int crcStart)
    {
        long calculated = 0; //Starting value

        int messageStart = 160;

        /* Iterate the set bits and XOR running checksum with lookup value */
        for(int i = message.nextSetBit(messageStart);
            i >= messageStart && i < crcStart;
            i = message.nextSetBit(i + 1))
        {
            calculated ^= checksums[i - messageStart];
        }

        long checksum = getLongChecksum(message, crcStart, 32);

        long error = calculated ^ checksum;

        if(error == 0 || error == 0xFFFFFFFFl)
        {
            message.setCRC(CRC.PASSED);

            return message;
        }
        else
        {
            int errorLocation = getBitError(error, checksums);

            if(errorLocation >= 0)
            {
                message.flip(errorLocation + messageStart);

                message.setCRC(CRC.CORRECTED);

                return message;
            }
        }

        message.setCRC(CRC.FAILED_CRC);

        return message;
    }

    /**
     * Error detection and correction of single-bit errors for CCITT 16-bit
     * CRC protected 80-bit messages.
     */
    public static BinaryMessage correctCCITT80(BinaryMessage message,
                                               int messageStart,
                                               int crcStart)
    {
        int calculated = 0; //Starting value

        /* Iterate the set bits and XOR running checksum with lookup value */
        for(int i = message.nextSetBit(messageStart);
            i >= messageStart && i < crcStart;
            i = message.nextSetBit(i + 1))
        {
            calculated ^= CCITT_80_CHECKSUMS[i - messageStart];
        }

        int checksum = getIntChecksum(message, crcStart, 16);

        int residual = calculated ^ checksum;

        if(residual == 0 || residual == 0xFFFF)
        {
            message.setCRC(CRC.PASSED);

            return message;
        }
        else
        {
            int errorLocation = getBitError(residual, CCITT_80_CHECKSUMS);

            if(errorLocation >= 0)
            {
                message.flip(errorLocation + messageStart);

                message.setCRC(CRC.CORRECTED);

                return message;
            }
        }

        message.setCRC(CRC.FAILED_CRC);

        return message;
    }

    /**
     * Error detection and correction of single-bit errors for CCITT 16-bit
     * CRC protected 80-bit messages.
     */
    public static int correctCCITT80(CorrectedBinaryMessage message, int messageStart, int crcStart)
    {
        int calculated = 0xFFFF; //Starting value

        /* Iterate the set bits and XOR running checksum with lookup value */
        for(int i = message.nextSetBit(messageStart); i >= messageStart && i < crcStart; i = message.nextSetBit(i + 1))
        {
            calculated ^= CCITT_80_CHECKSUMS[i - messageStart];
        }

        int checksum = getIntChecksum(message, crcStart, 16);

        int residual = calculated ^ checksum;

        if(residual == 0 || residual == 0xFFFF)
        {
            return 0;
        }
        else
        {
            int errorLocation = getBitError(residual, CCITT_80_CHECKSUMS);

            if(errorLocation >= 0)
            {
                message.flip(errorLocation + messageStart);
                message.incrementCorrectedBitCount(1);
                return 1;
            }
        }

        //Message has at least 2 bit errors - ie uncorrectable
        message.incrementCorrectedBitCount(2);

        return 2;
    }

    /**
     * Error detection for CRC-9 protected Confirmed Packet Data blocks.  These
     * data blocks have a slightly complicated structure because the checksum
     * is located between bits 7-15, within a 144 bit block.  The checksums
     * were generated assuming that the message is contiguous from 0 - 134 bits.
     * No data correction is performed.
     */
    public static CRC checkCRC9(BinaryMessage message, int messageStart)
    {
        int calculated = 0x0; //Initial fill of all ones

        /* Iterate the set bits and XOR running checksum with lookup value */
        for(int i = message.nextSetBit(messageStart);
            i >= messageStart && i < messageStart + 144;
            i = message.nextSetBit(i + 1))
        {
            /* message bits before the CRC */
            if(i < (messageStart + 7))
            {
                calculated ^= CRC9_CHECKSUMS[i - messageStart];
            }
            /* message bits after the CRC */
            else if(i > (messageStart + 15))
            {
                calculated ^= CRC9_CHECKSUMS[i - messageStart - 9];
            }
        }

        int checksum = message.getInt(messageStart + 7, messageStart + 15);

        int residual = calculated ^ checksum;

//		mLog.debug( "CALC:" + calculated + " CHECK:" + checksum + " RESID:" + residual );

        if(residual == 0 || residual == 0x1FF)
        {
            return CRC.PASSED;
        }

        return CRC.FAILED_CRC;
    }


    /**
     * Performs Galois 24/12/7 error detection and correction against the 12
     * encoded 24-bit message segments following the 64-bit NID in the message
     *
     * @return - true if all 12 segments of the message can be checked/corrected
     */
    public static boolean correctGalois24(CorrectedBinaryMessage tdulc)
    {
        boolean passes = true;

        int x = 64;

        while(x < tdulc.size() && passes)
        {

            int errors = Golay24.checkAndCorrect(tdulc, x);

            passes = errors < 2;

            x += 24;
        }

        return passes;
    }


    /**
     * Calculates the value of the message checksum as a long
     */
    public static long getLongChecksum(BinaryMessage message,
                                       int crcStart, int crcLength)
    {
        return message.getLong(crcStart, crcStart + crcLength - 1);
    }

    /**
     * Calculates the value of the message checksum as an integer
     */
    public static int getIntChecksum(BinaryMessage message,
                                     int crcStart, int crcLength)
    {
        return message.getInt(crcStart, crcStart + crcLength - 1);
    }

    /**
     * Identifies any single bit error position that matches the checksum error.
     */
    public static int getBitError(long checksumError, long[] checksums)
    {
        for(int x = 0; x < checksums.length; x++)
        {
            if(checksums[x] == checksumError)
            {
                return x;
            }
        }

        return -1;
    }

    /**
     * Identifies any single bit error position that matches the checksum error.
     */
    public static int getBitError(int checksumError, int[] checksums)
    {
        for(int x = 0; x < checksums.length; x++)
        {
            if(checksums[x] == checksumError)
            {
                return x;
            }
        }

        return -1;
    }

    public static void main(String[] args)
    {
        String raw = "000000001000001100000001010001111011000100001010010001111100000000000101000000000000000001000000000000110000000000000001101010101010101010101010";

        BinaryMessage message = BinaryMessage.load(raw);

        mLog.debug("MSG:" + message.toString());

        CRC results = checkCRC9(message, 0);

        mLog.debug("COR:" + message.toString());

        mLog.debug("Results: " + results.getDisplayText());
    }
}
