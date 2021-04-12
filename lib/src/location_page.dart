import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:gps_signal/src/banner_ad_container.dart';
import 'package:gps_signal/src/data.dart';
import 'package:gps_signal/src/share_btn.dart';
import 'package:gps_signal/src/speedometer_page.dart';
import 'package:location/location.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:share/share.dart';

LocationData? gLocationData;

class LocationPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 0.0,
        backgroundColor: Theme.of(context).scaffoldBackgroundColor,
        toolbarHeight: 10.0,
        /* toolbarHeight: 60.0,
        actions: [
          IconButton(
              icon: const Icon(Icons.more_vert),
              onPressed: () {
                print("onPressed");
              })
        ], */
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.only(bottom: 100.0),
        child: Column(
          children: [
            Padding(
              padding: const EdgeInsets.only(bottom: 24.0),
              child: Text(
                "Location data",
                style: GoogleFonts.montserrat(
                  textStyle: Theme.of(context).textTheme.bodyText1!.copyWith(
                        fontSize: 30.0,
                      ),
                ),
              ),
            ),
            StreamBuilder<LocationData>(
              stream: Location.instance.onLocationChanged,
              builder: (context, snapshot) {
                gLocationData = snapshot.data;
                final items =
                    collectListItems(snapshot.data ?? LocationData.fromMap({}));
                return ListView.separated(
                    shrinkWrap: true,
                    physics: const BouncingScrollPhysics(),
                    separatorBuilder: (_, __) => const Divider(height: 1.0),
                    itemCount: items.length,
                    itemBuilder: (_, i) {
                      final bool isForSpeed =
                          items[i].label.toLowerCase() == 'speed';
                      return ListTile(
                        title: Text(items[i].label),
                        subtitle: isForSpeed
                            ? const Text("Tab for speedometer")
                            : null,
                        trailing: Text(items[i].value),
                        onTap:
                            isForSpeed ? () => _openSpeedometer(context) : null,
                      );
                    });
              },
            ),
            if (!kIsWeb)
              Padding(
                padding: const EdgeInsets.symmetric(vertical: 24.0),
                child: BannerAdContainer(),
              ),
          ],
        ),
      ),
      floatingActionButton: ShareBtn(
        copy: () {
          print("copy ${gLocationData?.latitude}");
          final String text =
              collectListItems(gLocationData ?? LocationData.fromMap({}))
                  .map((e) => "${e.label}: ${e.value}")
                  .join("\n");

          Clipboard.setData(ClipboardData(text: text));

          ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
            content: Text("Copied to clipboard"),
          ));
        },
        share: () {
          print("share");
          Share.share(
            "http://www.google.com/maps/place/${gLocationData?.latitude},${gLocationData?.longitude}",
            subject: "My current location data",
          );
        },
      ),
    );
  }

  void _openSpeedometer(BuildContext context) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (_) => SpeedometerPage(),
        fullscreenDialog: true,
      ),
    );
  }
}
