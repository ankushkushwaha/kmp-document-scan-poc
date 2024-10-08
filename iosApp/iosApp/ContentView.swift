import SwiftUI
import Shared

struct ContentView: View {
    @State private var showContent = false
    @State var manager = DocumentScanner()
    @State var imagePath: String?
    
    var body: some View {
        VStack {
            
            Spacer()
            
            if let imagePath, let image = loadImage(from: imagePath) {
                Image(uiImage: image)
                    .resizable()
                    .scaledToFit()
                    .frame(width: 300, height: 300)
            } else {
                Text("Please scan document")
            }
            
            Spacer()
            
            Button("Open Camera") {
                manager.startScanning { result in
                    
                    print(result.pathList)
                    
                    if let path = result.pathList?.first {
                        imagePath = path
                    }
                }
            }
            
            Spacer()
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .padding()
    }
    
    func loadImage(from path: String) -> UIImage? {
        let fileManager = FileManager.default
        if fileManager.fileExists(atPath: path) {
            return UIImage(contentsOfFile: path)
        }
        return nil
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
