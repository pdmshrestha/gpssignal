import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:location/location.dart';

class SpeedometerPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 0.0,
        iconTheme: ThemeData.light().iconTheme,
        backgroundColor: Theme.of(context).scaffoldBackgroundColor,
      ),
      body: Center(
        child: StreamBuilder<LocationData>(
            stream: Location.instance.onLocationChanged,
            builder: (context, snapshot) {
              final data = snapshot.data ?? LocationData.fromMap({});
              return Padding(
                padding: const EdgeInsets.only(bottom: 80.0),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text(
                      _formatSpeed(data.speed),
                      style: GoogleFonts.orbitron(
                        textStyle: const TextStyle(fontSize: 100.0),
                      ),
                    ),
                    Padding(
                      padding: const EdgeInsets.only(left: 80.0),
                      child: Text(
                        "km/hr",
                        style: GoogleFonts.orbitron(
                          textStyle: const TextStyle(fontSize: 36.0),
                        ),
                      ),
                    ),
                  ],
                ),
              );
            }),
      ),
    );
  }

  String _formatSpeed(double? speed) {
    if (speed != null) {
      return "${speed * 3.6}";
    }
    return "-";
  }
}
