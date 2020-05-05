/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.tv.classics.presenters

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import coil.api.load
import com.android.tv.classics.models.TvMediaMetadata
import com.android.tv.classics.utils.TvLauncherUtils

/** Default height in DP used for card presenters, larger than this results in rows overflowing */
const val DEFAULT_CARD_HEIGHT: Int = 400

/** [Presenter] used to display a metadata item as an image card */
class TvMediaMetadataPresenter(private val cardHeight: Int = DEFAULT_CARD_HEIGHT) : Presenter() {

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) = Unit

    override fun onCreateViewHolder(parent: ViewGroup) =
            Presenter.ViewHolder(ImageCardView(parent.context).apply {
                isFocusable = true
                isFocusableInTouchMode = true
                // Set card background to dark gray while image loads
                setBackgroundColor(Color.DKGRAY)
                // Do not display text under the card image
                infoVisibility = View.GONE
            })

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
        // Cast item as a MediaMetadataCompat and viewholder's view as TextView
        val metadata = item as TvMediaMetadata
        val card = viewHolder.view as ImageCardView

        // Computes the card width from the given height and metadata aspect ratio
        val cardWidth = TvLauncherUtils.parseAspectRatio(metadata.artAspectRatio).let {
            cardHeight * it.numerator / it.denominator
        }

        card.titleText = metadata.title
        card.setMainImageDimensions(cardWidth, cardHeight)
        card.mainImageView.load(metadata.artUri)
    }
}