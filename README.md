# RouteOptimizer

A route optimization application that finds the most efficient path between two cities using the Uniform Cost Search (UCS) algorithm. Built with Java and Swing for a modern graphical interface.

## Features

- **UCS Algorithm**: Implements Uniform Cost Search algorithm to guarantee optimal route finding
- **Multiple Optimization Criteria**:
  - Distance-based optimization (minimize kilometers)
  - Cost-based optimization (minimize monetary cost in DT)
  - Hybrid mode (weighted combination of distance and cost)
- **Interactive GUI**: Modern Swing-based graphical interface with flat design aesthetics
- **Advanced Filtering**:
  - Highway restrictions (include/exclude highways)
  - Interdicted cities (avoid specific cities)
  - Budget constraints (set maximum cost limits)
- **Detailed Route Information**: View complete route details including segment-by-segment breakdown, total distance, and cost

## Project Structure

```
RouteOptimizer/
├── src/
│   ├── Routeoptimizer/
│   │   ├── RouteOptimizer.java      # Main GUI application
│   │   ├── GrapheRoutier.java       # Graph data structure for road network
│   │   ├── UCSAlgo.java             # UCS algorithm implementation
│   │   ├── Route.java               # Route segment model
│   │   ├── Segment.java             # Segment model
│   │   ├── Resultat.java            # Search result model
│   │   └── module-info.java         # Java module configuration
│   └── ...
├── bin/                             # Compiled class files
└── .project, .classpath             # Eclipse project files
```

## Getting Started

### Prerequisites

- Java 11 or higher
- Eclipse IDE (or any Java IDE)

### Installation

1. Clone the repository:
```bash
git clone https://github.com/ahmedbahroun06/RouteOptimizer.git
cd RouteOptimizer
```

2. Open the project in Eclipse or compile manually:
```bash
javac -d bin src/Routeoptimizer/*.java
```

3. Run the application:
```bash
java -cp bin Routeoptimizer.RouteOptimizer
```

## Usage

1. **Select Start City**: Choose your departure city from the dropdown menu
2. **Select End City**: Choose your destination city from the dropdown menu
3. **Choose Optimization Criteria**: Select from:
   - Distance (minimizes km)
   - Cost (minimizes DT)
   - Hybrid (weighted combination)
4. **Set Filters** (optional):
   - Toggle "Highway Only" to restrict routes to highways
   - Enter interdicted cities (comma-separated) to avoid specific cities
   - Set a budget limit if needed
5. **Search**: Click the search button to find the optimal route
6. **View Results**: See the complete route with segment details, total distance, and cost

## Algorithm

The application uses the **Uniform Cost Search (UCS)** algorithm:

- Explores nodes in order of increasing cumulative cost
- Guarantees optimal solution when all edge costs are >= 0
- Configurable cost function based on selected criteria
- Weighted formula for hybrid mode: `cost = 0.6 × distance + 0.4 × monetary_cost`

## Technical Details

- **Language**: Java 11+
- **GUI Framework**: Swing
- **Data Structure**: Adjacency list (HashMap-based)
- **Algorithm**: Uniform Cost Search (UCS)
- **Graph Type**: Undirected, weighted multigraph

## Architecture

### Key Classes

- **RouteOptimizer.java**: Main GUI application with all user interface components
- **GrapheRoutier.java**: Represents the road network as an undirected weighted graph
- **UCSAlgo.java**: Implements the Uniform Cost Search algorithm
- **Route.java**: Represents a single road segment between two cities
- **Resultat.java**: Contains search results and route information

### Color Scheme

Modern flat design palette with:
- Primary Blue: `#3B82F6`
- Secondary Green: `#10B981`
- Accent Red: `#EF4444`
- Light Gray backgrounds for accessibility

## Contributing

Feel free to fork this repository and submit pull requests for improvements.

## License

This project is provided as-is for educational and personal use.

## Author

Ahmed Bahroun - [@ahmedbahroun06](https://github.com/ahmedbahroun06)

---

**Note**: This is a Java Swing desktop application. Ensure you have a proper Java environment set up before running.
