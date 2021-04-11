import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:gps_signal/src/location_service.dart';

class PermissionPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Padding(
        padding: const EdgeInsets.all(24.0),
        child: Center(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            mainAxisAlignment: MainAxisAlignment.center,

            // ignore: prefer_const_literals_to_create_immutables
            children: [
              const Icon(
                Icons.location_off,
                size: 100.0,
                color: Colors.blueGrey,
              ),
              Padding(
                padding: const EdgeInsets.symmetric(vertical: 36.0),
                child: Text(
                  "App needs to access location. Please provide access by pressing button below",
                  textAlign: TextAlign.center,
                  style: GoogleFonts.lato(
                    textStyle: Theme.of(context).textTheme.bodyText1!.copyWith(
                          fontSize: 24.0,
                        ),
                  ),
                ),
              ),
              SizedBox(
                width: double.infinity,
                child: ElevatedButton(
                    onPressed: () {
                      final provider = context.read(locationProvider.notifier);
                      provider.requestPermission();
                    },
                    child: const Padding(
                      padding: EdgeInsets.all(16.0),
                      child: Text(
                        "Request permission",
                        style: TextStyle(fontSize: 20.0),
                      ),
                    )),
              )
            ],
          ),
        ),
      ),
    );
  }
}
