package org.riggvar.bo;

import org.riggvar.col.*;

public class TColCaptions {
    public static void InitDefaultColCaptions() {
        TColCaptionBag ColCaptionBag;
        ColCaptionBag = TBaseColProps.ColCaptionBag;

        // EventGrid.ColsActive.UseCustomColCaptions := True; //see Unit ColEvent

        // ---key named after Grid.Name: specific for this Grid-Instance
        // ColCaptionBag.SetCaption('EventGrid_col_GPoints', 'Points');

        // ---key named after EventNode.NameID: for all other Event-Displays
//        ColCaptionBag.setCaption("E_col_FN", "FN");
//        ColCaptionBag.setCaption("E_col_LN", "LN");
//        ColCaptionBag.setCaption("E_col_SN", "SN");
//        ColCaptionBag.setCaption("E_col_NC", "NC");
//        ColCaptionBag.setCaption("E_col_GR", "N5");
//        ColCaptionBag.setCaption("E_col_PB", "N6");

        ColCaptionBag.setCaption("E_col_SNR", "SNR");
        ColCaptionBag.setCaption("E_col_Bib", "Bib");

        ColCaptionBag.setCaption("E_col_GPoints", "Total");
        ColCaptionBag.setCaption("E_col_GRank", "Rank");
        ColCaptionBag.setCaption("E_col_GPosR", "PosR");
        ColCaptionBag.setCaption("E_col_Cup", "RLP");

        {
            ColCaptionBag.setCaption("RaceGrid_col_QU", "QU");
            ColCaptionBag.setCaption("RaceGrid_col_DG", "DG");
            ColCaptionBag.setCaption("RaceGrid_col_MRank", "MRank");
            ColCaptionBag.setCaption("RaceGrid_col_ORank", "ORank");
            ColCaptionBag.setCaption("RaceGrid_col_Rank", "Rank");
            ColCaptionBag.setCaption("RaceGrid_col_PosR", "PosR");
        }

        // set the persistent flag back to false,
        // do not save default values if these are the only overrides present
        ColCaptionBag.setIsPersistent(false);
    }
}
