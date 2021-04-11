import 'dart:io';

import 'package:flutter/material.dart';
import 'package:google_mobile_ads/google_mobile_ads.dart';

final BannerAd myBanner = BannerAd(
  adUnitId: Platform.isAndroid
      ? 'ca-app-pub-3752588937390537/7589925208'
      : 'ca-app-pub-3752588937390537/1252526203',
  size: AdSize.mediumRectangle,
  request: const AdRequest(),
  listener: const AdListener(),
);

class BannerAdContainer extends StatefulWidget {
  @override
  _BannerAdContainerState createState() => _BannerAdContainerState();
}

class _BannerAdContainerState extends State<BannerAdContainer> {
  final AdWidget adWidget = AdWidget(ad: myBanner);

  bool _isAdLoaded = false;

  @override
  void initState() {
    initAd();
    super.initState();
  }

  @override
  void dispose() {
    print("disposed");
    myBanner.dispose();
    super.dispose();
  }

  Future<void> initAd() async {
    final isLoaded = await myBanner.isLoaded();
    if (!isLoaded) {
      myBanner.load();
      setState(() => _isAdLoaded = true);
    }
  }

  @override
  Widget build(BuildContext context) {
    return _isAdLoaded
        ? Container(
            alignment: Alignment.center,
            width: myBanner.size.width.toDouble(),
            height: myBanner.size.height.toDouble(),
            child: adWidget,
          )
        : const SizedBox();
  }
}
