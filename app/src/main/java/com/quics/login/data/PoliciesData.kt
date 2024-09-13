package com.quics.login.data

import android.content.Context
import com.quics.login.R
import com.quics.login.models.PolicyItemModel

class PoliciesData(Ctx:Context) {
    val data = arrayOf(
        PolicyItemModel(Ctx.getString(R.string.terms_conditions),Ctx.getString(R.string.terms_conditions_short),1),
        PolicyItemModel(Ctx.getString(R.string.data_privacy),Ctx.getString(R.string.data_privacy_short),2),
        PolicyItemModel(Ctx.getString(R.string.cancel_returns),Ctx.getString(R.string.cancel_return_short),3),
        PolicyItemModel(Ctx.getString(R.string.email_help),Ctx.getString(R.string.email_help_short),4)
    )
}